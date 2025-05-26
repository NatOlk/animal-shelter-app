package com.ansh.stats.dto

data class TopicRequestStats(
    val topic: String = "",
    val approver: String = "",
    val count: Long
)