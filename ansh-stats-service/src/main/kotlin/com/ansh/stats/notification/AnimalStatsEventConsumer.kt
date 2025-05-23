package com.ansh.stats.notification

import com.ansh.event.AnimalShelterEvent
import com.ansh.stats.service.AnimalService
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class AnimalStatsEventConsumer(
    private val objectMapper: ObjectMapper,
    private val animalService: AnimalService
) {
    private val logger = LoggerFactory.getLogger(AnimalStatsEventConsumer::class.java)

    @KafkaListener(
        topics = ["\${animalTopicId}", "\${vaccinationTopicId}"],
        groupId = "statsGroupId"
    )
    fun listen(message: ConsumerRecord<String, String>) {
        try {
            val event = objectMapper.readValue(message.value(), AnimalShelterEvent::class.java)
            logger.info("[STATS] Received AnimalEvent: ${event.animal}")
            animalService.saveEvent(event)
        } catch (e: Exception) {
            logger.error("Failed to process AnimalEvent from Kafka: ${message.value()}", e)
        }
    }
}
