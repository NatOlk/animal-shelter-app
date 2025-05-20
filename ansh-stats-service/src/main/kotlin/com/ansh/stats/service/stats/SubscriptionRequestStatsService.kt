package com.ansh.stats.service.stats

import com.ansh.stats.dto.SubscriptionActionType
import com.ansh.stats.dto.TopicRequestStats
import com.ansh.stats.entity.SubscriptionDecisionEventDocument
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
        val events: List<SubscriptionDecisionEventDocument> = repository
            .findAllByActionType(SubscriptionActionType.REQUEST)

        return events
            .groupBy { it.payload.topic }
            .map { (topic, topicEvents) ->
                val cnt = topicEvents.count().toLong()
                TopicRequestStats(
                    topic = topic,
                    count = cnt,
                    approver = ""
                )
            }
    }

    fun getStatsByApprover(): List<TopicRequestStats> {
        val events: List<SubscriptionDecisionEventDocument> = repository
            .findAllByActionType(SubscriptionActionType.REQUEST)

        return events
            .groupBy { it.payload.approver }
            .map { (approver, topicEvents) ->
                val cnt = topicEvents.count().toLong()
                TopicRequestStats(
                    topic = "",
                    count = cnt,
                    approver = approver
                )
            }
    }
}
