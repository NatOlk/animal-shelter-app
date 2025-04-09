package com.ansh.stats.entity

import com.ansh.event.AnimalEvent
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "animal_events")
data class AnimalEventDocument(
    @Id val id: String? = null,
    val eventType: String,
    val payload: AnimalEvent,
    val receivedAt: LocalDateTime = LocalDateTime.now()
)