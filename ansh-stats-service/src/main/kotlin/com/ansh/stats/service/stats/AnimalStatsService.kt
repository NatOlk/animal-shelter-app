package com.ansh.stats.service.stats

import com.ansh.stats.constants.EventTypes
import com.ansh.stats.entity.AnimalLifespanStats
import com.ansh.stats.repository.AnimalEventRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Service
class AnimalStatsService(
    private val repository: AnimalEventRepository
) {
    private val logger = LoggerFactory.getLogger(AnimalStatsService::class.java)

    fun getAnimalCount(): Long {
        val count = repository.countByEventType(EventTypes.ADD_ANIMAL)
        logger.debug("Animal count (added): $count")
        return count
    }

    fun getAddedAnimalsGroupedByDate(): Map<LocalDate, Long> {
        val events = repository.findByEventType(EventTypes.ADD_ANIMAL)
        logger.debug("Found ${events.size} add animal events")
        val grouped = events.groupingBy { it.payload.created }.eachCount()
            .mapValues { it.value.toLong() }
        logger.debug("Grouped add animal events by date: {}", grouped)
        return grouped
    }

    fun getVaccinationCount(): Long {
        val count = repository.countByEventType(EventTypes.ADD_VACCINATION)
        logger.debug("Vaccination count: $count")
        return count
    }

    fun getAddedVaccinationsGroupedByDate(): Map<LocalDate, Long> {
        val events = repository.findByEventType(EventTypes.ADD_ANIMAL)
        logger.debug("Found ${events.size} add vaccination events")
        val grouped = events.groupingBy { it.payload.created }.eachCount()
            .mapValues { it.value.toLong() }
        logger.debug("Grouped vaccination events by date: {}", grouped)
        return grouped
    }

    fun getAnimalLifespans(): List<AnimalLifespanStats> {
        val allEvents = repository.findByEventTypeIn(
            listOf(
                EventTypes.ADD_ANIMAL,
                EventTypes.REMOVE_ANIMAL
            )
        )
        logger.debug("Found ${allEvents.size} total add/remove animal events")

        return allEvents
            .groupBy { it.animalId }
            .mapNotNull { (animalId, events) ->
                val addEvent =
                    events.find { it.eventType == EventTypes.ADD_ANIMAL }
                val removeEvent =
                    events.find { it.eventType == EventTypes.REMOVE_ANIMAL }

                if (addEvent != null && removeEvent != null) {

                    val days = ChronoUnit.DAYS.between(
                        addEvent.payload.created,
                        removeEvent.payload.created
                    )

                    val animal = addEvent.payload.animal
                    AnimalLifespanStats(
                        id = animal.id,
                        name = animal.name,
                        species = animal.species,
                        daysInSystem = days
                    )
                } else {
                    logger.warn("Missing add or remove event for animalId=$animalId")
                    null
                }
            }
    }
}
