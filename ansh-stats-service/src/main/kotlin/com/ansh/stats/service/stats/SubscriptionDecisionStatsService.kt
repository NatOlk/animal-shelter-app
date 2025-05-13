package com.ansh.stats.service.stats

import com.ansh.stats.entity.SubscriptionDecisionEventDocument
import com.ansh.stats.entity.TopicDecisionStats
import com.ansh.stats.repository.SubscriptionDecisionStatsRepository
import org.springframework.stereotype.Service

@Service
class SubscriptionDecisionStatsService(
    private val repository: SubscriptionDecisionStatsRepository
) {
    fun getCount(): Long {
        return repository.count();
    }

    fun getStatsByTopic(): List<TopicDecisionStats> {
        val events: List<SubscriptionDecisionEventDocument> = repository.findAll()

        return events
            .groupBy { it.payload.topic }
            .map { (topic, topicEvents) ->
                val approved = topicEvents.count { !it.payload.isReject }
                val rejected = topicEvents.count { it.payload.isReject }
                TopicDecisionStats(
                    topic = topic,
                    approvedCount = approved,
                    rejectedCount = rejected
                )
            }
    }
}
