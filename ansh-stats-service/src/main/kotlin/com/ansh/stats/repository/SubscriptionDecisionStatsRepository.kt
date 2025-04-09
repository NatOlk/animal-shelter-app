package com.ansh.stats.repository

import com.ansh.stats.entity.SubscriptionDecisionEventDocument
import org.springframework.data.mongodb.repository.MongoRepository

interface SubscriptionDecisionStatsRepository :
    MongoRepository<SubscriptionDecisionEventDocument, Long>
