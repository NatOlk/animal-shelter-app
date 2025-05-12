package com.ansh.stats.entity

data class AnimalLifespanStats(
    val id: Long,
    val name: String,
    val species: String,
    val daysInSystem: Long
)