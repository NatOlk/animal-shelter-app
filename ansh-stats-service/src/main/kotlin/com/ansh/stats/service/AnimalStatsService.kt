package com.ansh.stats.service

import com.ansh.event.AnimalEvent
import com.ansh.stats.entity.AnimalEventDocument
import com.ansh.stats.repository.AnimalEventRepository
import org.springframework.stereotype.Service

@Service
class AnimalStatsService(
    private val repository: AnimalEventRepository
) {

    fun saveEvent(event: AnimalEvent) {
        val document = AnimalEventDocument(
            eventType = event.javaClass.name,
            payload = event
        )
        repository.save(document)
    }
}
