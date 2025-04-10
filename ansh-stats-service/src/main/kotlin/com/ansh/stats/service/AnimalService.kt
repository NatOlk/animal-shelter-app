package com.ansh.stats.service

import com.ansh.event.AnimalEvent
import com.ansh.stats.entity.AnimalEventDocument
import com.ansh.stats.repository.AnimalEventRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class AnimalService(
    private val repository: AnimalEventRepository) {
    private val logger = LoggerFactory.getLogger(AnimalService::class.java)

    fun saveEvent(event: AnimalEvent) {
        event.animal.vaccinations?.forEach { it.animal = null }
        val document = AnimalEventDocument(
            eventType = event.javaClass.name,
            animalId = event.animal.id,
            payload = event
        )
        logger.debug("Saving animal event: $document")
        repository.save(document)
        logger.info("Animal event saved: type=${document.eventType}, animalId=${document.animalId}")
    }
}
