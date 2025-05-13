package com.ansh.stats.repository

import com.ansh.stats.dto.SubscriptionActionType
import com.ansh.stats.entity.SubscriptionDecisionEventDocument
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface SubscriptionDecisionStatsRepository :
    MongoRepository<SubscriptionDecisionEventDocument, String> {

    fun countByActionType(actionType: SubscriptionActionType): Long

    fun findAllByActionType(actionType: SubscriptionActionType): List<SubscriptionDecisionEventDocument>

    fun findAllByActionTypeIn(actionTypes: List<SubscriptionActionType>): List<SubscriptionDecisionEventDocument>
}
