package com.ansh.stats.service.stats

import com.ansh.stats.constants.EventTypes
import com.ansh.stats.dto.AnimalLifespanStats
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
        return repository.aggregateEventCountByDate(EventTypes.ADD_ANIMAL)
            .associate { it.date to it.count }
    }

    fun getVaccinationCount(): Long {
        val count = repository.countByEventType(EventTypes.ADD_VACCINATION)
        logger.debug("Vaccination count: $count")
        return count
    }

    fun getAddedVaccinationsGroupedByDate(): Map<LocalDate, Long> {
        return repository.aggregateEventCountByDate(EventTypes.ADD_VACCINATION)
            .associate { it.date to it.count }
    }

    fun getAnimalLifespans(): List<AnimalLifespanStats> {
        return repository.aggregateAnimalLifespans()
    }
}
