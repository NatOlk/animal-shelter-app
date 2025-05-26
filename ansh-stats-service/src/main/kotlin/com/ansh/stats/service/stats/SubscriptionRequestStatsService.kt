package com.ansh.stats.service.stats

import com.ansh.stats.dto.SubscriptionActionType
import com.ansh.stats.dto.TopicRequestStats
import com.ansh.stats.entity.SubscriptionDecisionEventDocument
import com.ansh.stats.repository.SubscriptionDecisionStatsRepository
import org.springframework.stereotype.Service
import com.ansh.stats.utils.groupAndMap

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

        return groupAndMap(
            items = events,
            keySelector = { it.payload.topic },
            valueMapper = { topic, topicEvents ->
                TopicRequestStats(topic, approver = "", topicEvents.size.toLong())
            }
        )
    }

    fun getStatsByApprover(): List<TopicRequestStats> {
        val events: List<SubscriptionDecisionEventDocument> = repository
            .findAllByActionType(SubscriptionActionType.REQUEST)

        return groupAndMap(
            items = events,
            keySelector = { it.payload.approver },
            valueMapper = { approver, topicEvents ->
                TopicRequestStats(topic = "", approver, topicEvents.size.toLong())
            }
        )
    }
}
