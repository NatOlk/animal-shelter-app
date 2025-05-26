package com.ansh.stats.service.stats

import com.ansh.stats.dto.SubscriptionActionType
import com.ansh.stats.dto.TopicDecisionStats
import com.ansh.stats.repository.SubscriptionDecisionStatsRepository
import org.springframework.stereotype.Service

@Service
class SubscriptionDecisionStatsService(
    private val repository: SubscriptionDecisionStatsRepository
) {
    fun getCount(): Long {
        return repository.countByActionType(SubscriptionActionType.DECISION);
    }

    fun getStatsByTopic(): List<TopicDecisionStats> {
        return repository.aggregateDecisionsByTopic(SubscriptionActionType.DECISION.name)
    }
}
