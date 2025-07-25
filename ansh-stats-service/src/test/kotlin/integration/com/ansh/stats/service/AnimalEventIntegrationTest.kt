package com.ansh.stats.service

import com.ansh.entity.animal.Animal
import com.ansh.event.animal.AddAnimalEvent
import com.ansh.stats.repository.AnimalEventRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import java.time.LocalDate
import java.util.*

@SpringBootTest
@Testcontainers
class AnimalEventIntegrationTest {

    companion object {
        @Container
        val kafka = KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.5.1"))

        @Container
        val mongo = MongoDBContainer(DockerImageName.parse("mongo:6.0"))

        @JvmStatic
        @DynamicPropertySource
        fun registerProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers)
            registry.add("spring.data.mongodb.uri", mongo::getReplicaSetUrl)
        }
    }

    @Autowired
    lateinit var kafkaTemplate: KafkaTemplate<String, String>

    @Autowired
    lateinit var repository: AnimalEventRepository

    @Autowired
    lateinit var objectMapper: com.fasterxml.jackson.databind.ObjectMapper

    @BeforeEach
    fun cleanDatabase() {
        repository.deleteAll()
    }

    @Test
    fun `send Kafka message and verify Mongo document is saved`() {
        // Arrange
        val animal = Animal()
        animal.id = 42L
        animal.name = "Luna"
        animal.species = "Cat"
        animal.admissionDate = LocalDate.now()

        val event = AddAnimalEvent(animal)
        event.eventId = "1-1-1"
        val json = objectMapper.writeValueAsString(event)

        kafkaTemplate.send("animalTopicId", UUID.randomUUID().toString(), json)

        // Wait a little to let the consumer process
        Thread.sleep(2000)

        // Assert
        val all = repository.findAll()
        val saved = all.firstOrNull { it.animalId == animal.id }

        assertEquals(animal.name, saved?.payload?.animal?.name)
        assertEquals("com.ansh.event.animal.AddAnimalEvent", saved?.eventType)
    }

    @Test
    fun `send two Kafka message with equals eventId and verify only one Mongo document is saved`() {
        // Arrange
        val animal = Animal()
        animal.id = 42L
        animal.name = "Luna"
        animal.species = "Cat"
        animal.admissionDate = LocalDate.now()

        val animal2 = Animal()
        animal2.id = 43L
        animal2.name = "Bobik"
        animal2.species = "Dog"
        animal2.admissionDate = LocalDate.now()

        val event = AddAnimalEvent(animal)
        event.eventId = "1-1-1"
        val json = objectMapper.writeValueAsString(event)

        kafkaTemplate.send("animalTopicId", UUID.randomUUID().toString(), json)

        val event2 = AddAnimalEvent(animal2)
        event2.eventId = "1-1-1"
        val json2 = objectMapper.writeValueAsString(event2)

        kafkaTemplate.send("animalTopicId", UUID.randomUUID().toString(), json2)

        // Wait a little to let the consumer process
        Thread.sleep(2000)

        // Assert
        val all = repository.findAll()
        System.out.println(all)
        val saved = all.firstOrNull { it.animalId == animal.id }

        assertEquals(animal.name, saved?.payload?.animal?.name)
        assertEquals("com.ansh.event.animal.AddAnimalEvent", saved?.eventType)
        assertEquals(all.size, 1)
    }

    @Test
    fun `send two Kafka message with diff eventId and verify two Mongo document is saved`() {
        // Arrange
        val animal = Animal()
        animal.id = 42L
        animal.name = "Luna"
        animal.species = "Cat"
        animal.admissionDate = LocalDate.now()

        val animal2 = Animal()
        animal2.id = 43L
        animal2.name = "Bobik"
        animal2.species = "Dog"
        animal2.admissionDate = LocalDate.now()

        val event = AddAnimalEvent(animal)
        event.eventId = "2-1-1"
        val json = objectMapper.writeValueAsString(event)

        kafkaTemplate.send("animalTopicId", UUID.randomUUID().toString(), json)

        val event2 = AddAnimalEvent(animal2)
        event2.eventId = "1-1-1"
        val json2 = objectMapper.writeValueAsString(event2)

        kafkaTemplate.send("animalTopicId", UUID.randomUUID().toString(), json2)

        // Wait a little to let the consumer process
        Thread.sleep(2000)

        // Assert
        val all = repository.findAll()
        val saved = all.firstOrNull { it.animalId == animal.id }

        assertEquals(animal.name, saved?.payload?.animal?.name)
        assertEquals("com.ansh.event.animal.AddAnimalEvent", saved?.eventType)
        assertEquals(all.size, 2)
    }
}
