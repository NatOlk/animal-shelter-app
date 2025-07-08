package com.ansh.stats.service

import com.ansh.event.AnimalShelterEvent
import com.ansh.stats.repository.AnimalEventRepository
import com.ansh.stats.utils.saveAndLog
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

        if (repository.existsByEventId(document.eventId)) {
            logger.info("Event with ID ${document.eventId} already processed. Skipping.")
            return
        }
        saveAndLog(
            item = document,
            save = repository::save,
            logBefore = { logger.debug("Saving animal event: {}", it) },
            logAfter = { logger.debug("Animal event saved: type=${document.eventType}, " +
                    "animalId=${document.animalId}") }
        )
    }
}
