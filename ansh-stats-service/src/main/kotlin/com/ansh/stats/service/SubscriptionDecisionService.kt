package com.ansh.stats.service

import com.ansh.event.subscription.SubscriptionDecisionEvent
import com.ansh.stats.dto.SubscriptionActionType
import com.ansh.stats.entity.SubscriptionDecisionEventDocument
import com.ansh.stats.repository.SubscriptionDecisionStatsRepository
import org.springframework.stereotype.Service

@Service
class SubscriptionDecisionService(
    private val repository: SubscriptionDecisionStatsRepository
) {
    fun saveDecisionEvent(event: SubscriptionDecisionEvent) {
        val stats = SubscriptionDecisionEventDocument(
            payload = event,
            actionType = SubscriptionActionType.DECISION
        )
        repository.save(stats)
    }

    fun saveRequestEvent(event: SubscriptionDecisionEvent) {
        val stats = SubscriptionDecisionEventDocument(
            payload = event,
            actionType = SubscriptionActionType.REQUEST
        )
        repository.save(stats)
    }
}

