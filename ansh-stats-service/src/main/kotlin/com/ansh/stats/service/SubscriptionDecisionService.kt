package com.ansh.stats.service

import com.ansh.event.subscription.SubscriptionDecisionEvent
import com.ansh.stats.dto.SubscriptionActionType
import com.ansh.stats.repository.SubscriptionDecisionStatsRepository
import com.ansh.stats.utils.saveAndLog
import com.ansh.stats.utils.toDocument
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class SubscriptionDecisionService(
    private val repository: SubscriptionDecisionStatsRepository
) {
    private val logger = LoggerFactory.getLogger(SubscriptionDecisionService::class.java)

    fun saveDecisionEvent(event: SubscriptionDecisionEvent) {
        saveEventWithType(event, SubscriptionActionType.DECISION)
    }

    fun saveRequestEvent(event: SubscriptionDecisionEvent) {
        saveEventWithType(event, SubscriptionActionType.REQUEST)
    }

    private fun saveEventWithType(
        event: SubscriptionDecisionEvent,
        type: SubscriptionActionType
    ) {
        val document = event.toDocument(type)
        saveAndLog(
            item = document,
            save = repository::save,
            logAfter = { logger.info("Saved decision event: {}", it) }
        )
    }
}

