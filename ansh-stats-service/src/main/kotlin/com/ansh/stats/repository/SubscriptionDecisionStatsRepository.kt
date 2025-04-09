package com.ansh.stats.repository

import com.ansh.stats.entity.SubscriptionDecisionEventDocument
import org.springframework.data.jpa.repository.JpaRepository

interface SubscriptionDecisionStatsRepository : JpaRepository<SubscriptionDecisionEventDocument, Long>
