package com.ansh.stats.service

import com.ansh.stats.entity.AnimalLifespanStats
import com.ansh.stats.repository.AnimalEventRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.LocalDate

@Service
class AnimalStatsService(
    private val repository: AnimalEventRepository
) {
    private val logger = LoggerFactory.getLogger(AnimalStatsService::class.java)

    fun getAnimalCount(): Long {
        val count = repository.countByEventType("com.ansh.event.AddAnimalEvent")
        logger.debug("Animal count (added): $count")
        return count
    }

    fun getAddedAnimalsGroupedByDate(): Map<LocalDate, Long> {
        val events = repository.findByEventType("com.ansh.event.AddAnimalEvent")
        logger.debug("Found ${events.size} add animal events")
        val grouped = events.groupingBy { it.receivedAt.toLocalDate() }.eachCount()
            .mapValues { it.value.toLong() }
        logger.debug("Grouped add animal events by date: $grouped")
        return grouped
    }

    fun getVaccinationCount(): Long {
        val count = repository.countByEventType("addVaccinationEvent")
        logger.debug("Vaccination count: $count")
        return count
    }

    fun getAddedVaccinationsGroupedByDate(): Map<LocalDate, Long> {
        val events = repository.findByEventType("com.ansh.event.AddVaccinationEvent")
        logger.debug("Found ${events.size} add vaccination events")
        val grouped = events.groupingBy { it.receivedAt.toLocalDate() }.eachCount()
            .mapValues { it.value.toLong() }
        logger.debug("Grouped vaccination events by date: $grouped")
        return grouped
    }

    fun getAnimalLifespans(): List<AnimalLifespanStats> {
        val allEvents = repository.findByEventTypeIn(
            listOf(
                "com.ansh.event.AddAnimalEvent",
                "com.ansh.event.RemoveAnimalEvent"
            )
        )
        logger.debug("Found ${allEvents.size} total add/remove animal events")

        return allEvents
            .groupBy { it.animalId }
            .mapNotNull { (animalId, events) ->
                val addEvent = events.find { it.eventType == "com.ansh.event.AddAnimalEvent" }
                val removeEvent = events.find { it.eventType == "com.ansh.event.RemoveAnimalEvent" }

                if (addEvent != null && removeEvent != null) {
                    val duration = Duration.between(addEvent.receivedAt, removeEvent.receivedAt)
                    val animal = addEvent.payload.animal
                    AnimalLifespanStats(
                        id = animal.id,
                        name = animal.name,
                        species = animal.species,
                        daysInSystem = duration.toDays(),
                        hoursInSystem = duration.toHours() % 24,
                        minutesInSystem = duration.toMinutes() % 60
                    )
                } else {
                    logger.warn("Missing add or remove event for animalId=$animalId")
                    null
                }
            }
    }
}
