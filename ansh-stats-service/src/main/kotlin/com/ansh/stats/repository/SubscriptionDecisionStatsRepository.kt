package com.ansh.stats.repository

import com.ansh.stats.dto.SubscriptionActionType
import com.ansh.stats.dto.TopicDecisionStats
import com.ansh.stats.dto.TopicRequestStats
import com.ansh.stats.entity.SubscriptionDecisionEventDocument
import org.springframework.data.mongodb.repository.Aggregation
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface SubscriptionDecisionStatsRepository :
    MongoRepository<SubscriptionDecisionEventDocument, String> {

    fun countByActionType(actionType: SubscriptionActionType): Long
    fun existsByEventId(eventId: String): Boolean
    @Aggregation(
        pipeline = [
            "{ '\$match': { 'actionType': ?0 } }",
            "{ '\$group': { '_id': '\$payload.topic', 'count': { '\$sum': 1 } } }",
            "{ '\$project': { 'topic': '\$_id', 'count': 1, '_id': 0 } }"
        ]
    )
    fun aggregateRequestsByTopic(actionType: String): List<TopicRequestStats>

    @Aggregation(
        pipeline = [
            "{ '\$match': { 'actionType': ?0 } }",
            "{ '\$group': { '_id': '\$payload.approver', 'count': { '\$sum': 1 } } }",
            "{ '\$project': { 'approver': '\$_id', 'count': 1, '_id': 0 } }"
        ]
    )
    fun aggregateRequestsByApprover(actionType: String): List<TopicRequestStats>

    @Aggregation(
        pipeline = [
            "{ '\$match': { 'actionType': ?0 } }",
            "{ '\$group': { " +
                    "'_id': '\$payload.topic', " +
                    "'approvedCount': { '\$sum': { '\$cond': [ { '\$eq': [ '\$payload.reject', false ] }, 1, 0 ] } }, " +
                    "'rejectedCount': { '\$sum': { '\$cond': [ { '\$eq': [ '\$payload.reject', true ] }, 1, 0 ] } }, " +
                    "'count': { '\$sum': 1 } " +
                    "} }",
            "{ '\$project': { 'topic': '\$_id', 'approvedCount': 1, 'rejectedCount': 1, 'count': 1, '_id': 0 } }"
        ]
    )
    fun aggregateDecisionsByTopic(actionType: String): List<TopicDecisionStats>
}
