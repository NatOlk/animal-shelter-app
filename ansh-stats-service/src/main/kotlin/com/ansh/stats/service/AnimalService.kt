package com.ansh.stats.service

import com.ansh.event.AnimalShelterEvent
import com.ansh.stats.repository.AnimalEventRepository
import com.ansh.stats.utils.toDocument
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class AnimalService(
    private val repository: AnimalEventRepository
) {
    private val logger = LoggerFactory.getLogger(AnimalService::class.java)

    fun saveEvent(event: AnimalShelterEvent) {
        val document = event.toDocument()
        logger.debug("Saving animal event: {}", document)
        repository.save(document)
        logger.info("Animal event saved: type=${document.eventType}, animalId=${document.animalId}")
    }
}
