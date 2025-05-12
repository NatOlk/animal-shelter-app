package com.ansh.stats.notification

import com.ansh.event.subscription.SubscriptionDecisionEvent
import com.ansh.stats.service.SubscriptionService
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class SubscriptionDecisionStatsEventConsumer(
    private val objectMapper: ObjectMapper,
    private val statsService: SubscriptionService
) {

    private val logger = LoggerFactory.getLogger(SubscriptionDecisionStatsEventConsumer::class.java)

    @KafkaListener(topics = ["\${approveTopicId}"], groupId = "statsGroup")
    fun listen(message: ConsumerRecord<String, String>) {
        try {
            val event =
                objectMapper.readValue(message.value(), SubscriptionDecisionEvent::class.java)

            logger.info(
                "[STATS SERVICE] Received SubscriptionDecisionEvent: email=${event.email}, approver=${event.approver}, reject=${event.isReject}"
            )
            statsService.saveEvent(event)

        } catch (e: Exception) {
            logger.error("Failed to process event: ${message.value()}", e)
        }
    }
}
