package com.ansh.stats.service

import com.ansh.event.subscription.SubscriptionDecisionEvent
import com.ansh.stats.entity.SubscriptionDecisionEventDocument
import com.ansh.stats.repository.SubscriptionDecisionStatsRepository
import org.springframework.stereotype.Service

@Service
class SubscriptionStatsService(
    private val repository: SubscriptionDecisionStatsRepository
) {
    fun saveEvent(event: SubscriptionDecisionEvent) {
        val stats = SubscriptionDecisionEventDocument(
            payload = event
        )
        repository.save(stats)
    }
}

