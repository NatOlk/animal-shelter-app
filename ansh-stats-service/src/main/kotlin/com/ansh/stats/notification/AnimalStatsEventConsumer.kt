package com.ansh.stats.notification

import com.ansh.event.AnimalShelterEvent
import com.ansh.stats.service.AnimalService
import com.ansh.stats.utils.processAndLogKafkaMessage
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Service

@Service
class AnimalStatsEventConsumer(
    private val objectMapper: ObjectMapper,
    private val animalService: AnimalService
) {
    private val logger = LoggerFactory.getLogger(AnimalStatsEventConsumer::class.java)

    @KafkaListener(
        topics = ["\${animalTopicId}", "\${vaccinationTopicId}"],
        groupId = "statsGroupId",
        containerFactory = "manualAckFactory"
    )
    fun listen(message: ConsumerRecord<String, String>, ack: Acknowledgment) {
        processAndLogKafkaMessage<AnimalShelterEvent>(
            message = message,
            objectMapper = objectMapper,
            logBefore = { logger.debug("Parsed event: {}", it) },
            logAfter = { logger.debug("Event successfully processed: {}", it) },
            logError = { ex ->
                logger.error(
                    "Failed to process AnimalShelterEvent from Kafka: ${message.value()}", ex
                )
            },
            onSuccess = { event ->
                animalService.saveEvent(event)
                ack.acknowledge()
            }
        )
    }
}