package com.ansh.stats.service.stats

import com.ansh.stats.dto.SubscriptionActionType
import com.ansh.stats.dto.TopicDecisionStats
import com.ansh.stats.entity.SubscriptionDecisionEventDocument
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
        val events: List<SubscriptionDecisionEventDocument> = repository
            .findAllByActionType(SubscriptionActionType.DECISION)

        return events
            .groupBy { it.payload.topic }
            .map { (topic, topicEvents) ->
                val approved = topicEvents.count { !it.payload.isReject }.toLong()
                val rejected = topicEvents.count { it.payload.isReject }.toLong()
                TopicDecisionStats(
                    topic = topic,
                    approvedCount = approved,
                    rejectedCount = rejected,
                    count = approved + rejected
                )
            }
    }
}
