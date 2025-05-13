package com.ansh.stats.entity

data class TopicDecisionStats(
    val topic: String,
    val approvedCount: Int,
    val rejectedCount: Int
)