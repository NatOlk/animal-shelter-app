package com.ansh.stats.dto

data class AnimalLifespanStats(
    val animalId: Long,
    val name: String,
    val species: String,
    val daysInSystem: Long
)