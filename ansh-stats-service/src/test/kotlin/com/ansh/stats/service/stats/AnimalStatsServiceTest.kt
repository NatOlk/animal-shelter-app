package com.ansh.stats.service.stats

import com.ansh.stats.constants.EventTypes
import com.ansh.stats.dto.AnimalLifespanStats
import com.ansh.stats.dto.EventCountByDate
import com.ansh.stats.repository.AnimalEventRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.LocalDate

class AnimalStatsServiceTest {

    private val repository: AnimalEventRepository = Mockito.mock(AnimalEventRepository::class.java)
    private val service = AnimalStatsService(repository)

    @Test
    fun `getAnimalCount returns correct count`() {
        whenever(repository.countByEventType(EventTypes.ADD_ANIMAL)).thenReturn(42L)

        val result = service.getAnimalCount()

        assertEquals(42L, result)
        verify(repository).countByEventType(EventTypes.ADD_ANIMAL)
    }

    @Test
    fun `getVaccinationCount returns correct count`() {
        whenever(repository.countByEventType(EventTypes.ADD_VACCINATION)).thenReturn(7L)

        val result = service.getVaccinationCount()

        assertEquals(7L, result)
        verify(repository).countByEventType(EventTypes.ADD_VACCINATION)
    }

    @Test
    fun `getAddedAnimalsGroupedByDate groups by payload created date`() {
        val date = LocalDate.of(2025, 5, 10)
        val eventCountByDate = EventCountByDate(date, 1)

        whenever(repository.aggregateEventCountByDate(EventTypes.ADD_ANIMAL))
            .thenReturn(listOf(eventCountByDate))

        val result = service.getAddedAnimalsGroupedByDate()

        assertEquals(mapOf(date to 1L), result)
    }

    @Test
    fun `getAddedVaccinationsGroupedByDate groups by payload created date`() {
        val date = LocalDate.of(2025, 5, 10)
        val eventCountByDate = EventCountByDate(date, 1)

        whenever(repository.aggregateEventCountByDate(EventTypes.ADD_VACCINATION))
            .thenReturn(listOf(eventCountByDate))

        val result = service.getAddedVaccinationsGroupedByDate()

        assertEquals(mapOf(date to 1L), result)
    }

    @Test
    fun `getAnimalLifespans returns correct aggregated data`() {
        val expected = AnimalLifespanStats(
            animalId = 1L,
            name = "Bella",
            species = "Dog",
            daysInSystem = 3
        )

        whenever(repository.aggregateAnimalLifespans())
            .thenReturn(listOf(expected))

        val results = service.getAnimalLifespans()

        assertEquals(1, results.size)
        val lifespan = results[0]
        assertEquals(expected.animalId, lifespan.animalId)
        assertEquals(expected.name, lifespan.name)
        assertEquals(expected.species, lifespan.species)
        assertEquals(expected.daysInSystem, lifespan.daysInSystem)
    }
}
