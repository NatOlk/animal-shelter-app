package com.ansh.stats.service

import com.ansh.event.subscription.SubscriptionDecisionEvent
import com.ansh.stats.dto.SubscriptionActionType
import com.ansh.stats.repository.SubscriptionDecisionStatsRepository
import com.ansh.stats.utils.toDocument
import org.springframework.stereotype.Service

@Service
class SubscriptionDecisionService(
    private val repository: SubscriptionDecisionStatsRepository
) {
    fun saveDecisionEvent(event: SubscriptionDecisionEvent) {
        repository.save(event.toDocument(SubscriptionActionType.DECISION))
    }

    fun saveRequestEvent(event: SubscriptionDecisionEvent) {
        repository.save(event.toDocument(SubscriptionActionType.REQUEST))
    }
}

