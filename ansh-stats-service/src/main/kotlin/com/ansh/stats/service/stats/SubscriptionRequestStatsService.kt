package com.ansh.stats.service.stats

import com.ansh.stats.dto.SubscriptionActionType
import com.ansh.stats.dto.TopicRequestStats
import com.ansh.stats.repository.SubscriptionDecisionStatsRepository
import org.springframework.stereotype.Service

@Service
class SubscriptionRequestStatsService(
    private val repository: SubscriptionDecisionStatsRepository
) {
    fun getCount(): Long {
        return repository.countByActionType(SubscriptionActionType.REQUEST);
    }

    fun getStatsByTopic(): List<TopicRequestStats> {
        return repository.aggregateRequestsByTopic(SubscriptionActionType.REQUEST.name)
    }

    fun getStatsByApprover(): List<TopicRequestStats> {
        return repository.aggregateRequestsByApprover(SubscriptionActionType.REQUEST.name)
    }
}
