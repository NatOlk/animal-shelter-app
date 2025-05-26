package com.ansh.stats.notification

import com.ansh.event.subscription.SubscriptionDecisionEvent
import com.ansh.stats.service.SubscriptionDecisionService
import com.ansh.stats.utils.processAndLogKafkaMessage
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class SubscriptionDecisionStatsEventConsumer(
    private val objectMapper: ObjectMapper,
    private val statsService: SubscriptionDecisionService
) {
    private val logger = LoggerFactory.getLogger(SubscriptionDecisionStatsEventConsumer::class.java)

    @KafkaListener(topics = ["\${approveTopicId}"], groupId = "statsGroup")
    fun listenApproveTopic(message: ConsumerRecord<String, String>) {
        handleSubscriptionEvent(
            message = message,
            logBeforeMessage = "[stat service] Received SubscriptionDecisionEvent",
            onSuccess = statsService::saveDecisionEvent
        )
    }

    @KafkaListener(topics = ["\${subscriptionTopicId}"], groupId = "statsGroup")
    fun listenSubscriptionTopic(message: ConsumerRecord<String, String>) {
        handleSubscriptionEvent(
            message = message,
            logBeforeMessage = "[stat service] Received SubscriptionRequestEvent",
            onSuccess = statsService::saveRequestEvent
        )
    }

    private fun handleSubscriptionEvent(
        message: ConsumerRecord<String, String>,
        logBeforeMessage: String,
        onSuccess: (SubscriptionDecisionEvent) -> Unit
    ) {
        processAndLogKafkaMessage(
            message = message,
            objectMapper = objectMapper,
            logBefore = {
                logger.debug(
                    "$logBeforeMessage: email=${it.email}, approver=${it.approver}, reject=${it.isReject}"
                )
            },
            logAfter = {
                logger.debug("Event successfully processed: {}", it)
            },
            logError = { ex ->
                logger.error("Failed to process SubscriptionDecisionEvent: ${message.value()}", ex)
            },
            onSuccess = onSuccess
        )
    }
}
