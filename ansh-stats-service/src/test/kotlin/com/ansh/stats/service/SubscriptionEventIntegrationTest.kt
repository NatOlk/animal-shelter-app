package com.ansh.stats.service

import com.ansh.event.subscription.SubscriptionDecisionEvent
import com.ansh.stats.dto.SubscriptionActionType
import com.ansh.stats.repository.SubscriptionDecisionStatsRepository
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
import java.util.*

@SpringBootTest
@Testcontainers
class SubscriptionEventIntegrationTest {

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
    lateinit var repository: SubscriptionDecisionStatsRepository

    @Autowired
    lateinit var objectMapper: com.fasterxml.jackson.databind.ObjectMapper

    @BeforeEach
    fun cleanDatabase() {
        repository.deleteAll()
    }

    @Test
    fun `send SubscriptionDecisionEvent message and verify Mongo document is saved`() {
        // Arrange
        val event = SubscriptionDecisionEvent()
        event.eventId = "1-1-1"
        event.topic = "topic"
        val json = objectMapper.writeValueAsString(event)

        kafkaTemplate.send("approveTopicId", UUID.randomUUID().toString(), json)

        // Wait a little to let the consumer process
        Thread.sleep(2000)

        // Assert
        val all = repository.findAll()
        val saved = all.firstOrNull { it.eventId == "1-1-1" }

        assertEquals(event.eventId, saved?.eventId)
        assertEquals(SubscriptionActionType.DECISION, saved?.actionType)
    }

    @Test
    fun `send SubscriptionRequestEvent message and verify Mongo document is saved`() {
        // Arrange
        val event = SubscriptionDecisionEvent()
        event.eventId = "1-1-1"
        event.topic = "topic"
        val json = objectMapper.writeValueAsString(event)

        kafkaTemplate.send("subscriptionTopicId", UUID.randomUUID().toString(), json)

        // Wait a little to let the consumer process
        Thread.sleep(2000)

        // Assert
        val all = repository.findAll()
        val saved = all.firstOrNull { it.eventId == "1-1-1" }

        assertEquals(event.eventId, saved?.eventId)
        assertEquals(SubscriptionActionType.REQUEST, saved?.actionType)
    }

    @Test
    fun `send two SubscriptionRequestEvent message and verify only one Mongo document is saved`() {
        // Arrange
        val event = SubscriptionDecisionEvent()
        event.eventId = "1-1-1"
        event.topic = "topic"
        val json = objectMapper.writeValueAsString(event)

        kafkaTemplate.send("subscriptionTopicId", UUID.randomUUID().toString(), json)

        val event1 = SubscriptionDecisionEvent()
        event1.eventId = "1-1-1"
        event1.topic = "topic"
        val json1 = objectMapper.writeValueAsString(event1)

        kafkaTemplate.send("subscriptionTopicId", UUID.randomUUID().toString(), json1)

        // Wait a little to let the consumer process
        Thread.sleep(2000)

        // Assert
        val all = repository.findAll()
        val saved = all.firstOrNull { it.eventId == "1-1-1" }

        assertEquals(event.eventId, saved?.eventId)
        assertEquals(SubscriptionActionType.REQUEST, saved?.actionType)

        assertEquals(all.size, 1)
    }
}
