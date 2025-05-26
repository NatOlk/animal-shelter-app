package com.ansh.stats.dto

import java.time.LocalDate

data class EventCountByDate(
    val date: LocalDate,
    val count: Long
)
