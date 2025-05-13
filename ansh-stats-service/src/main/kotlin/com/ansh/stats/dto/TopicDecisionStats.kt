package com.ansh.stats.dto

data class TopicDecisionStats(
    val topic: String,
    val count: Long,
    val approvedCount: Long,
    val rejectedCount: Long
)