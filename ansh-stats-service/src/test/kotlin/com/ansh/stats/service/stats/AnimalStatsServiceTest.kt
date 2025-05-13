package com.ansh.stats.service.stats

import com.ansh.entity.animal.Animal
import com.ansh.event.animal.AddAnimalEvent
import com.ansh.event.animal.RemoveAnimalEvent
import com.ansh.stats.constants.EventTypes
import com.ansh.stats.entity.AnimalEventDocument
import com.ansh.stats.dto.AnimalLifespanStats
import com.ansh.stats.repository.AnimalEventRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.time.LocalDate
import org.mockito.kotlin.*
import java.time.LocalDateTime

class AnimalStatsServiceTest {

    private val repository: AnimalEventRepository = Mockito.mock(AnimalEventRepository::class.java)
    private val service = AnimalStatsService(repository)

    @Test
    fun `getAnimalLifespans returns correct lifespan stats`() {
        val now = LocalDate.now()
        val createdAt = now.minusDays(2)
        val animal = Animal().apply {
            id = 1L
            name = "Bella"
            species = "Dog"
        }

        val addEventPayload = AddAnimalEvent(animal).apply {
            created = createdAt
        }

        val addEvent = AnimalEventDocument(
            id = "1",
            eventType = EventTypes.ADD_ANIMAL,
            animalId = animal.id,
            payload = addEventPayload
        )

        val removeEvent = AnimalEventDocument(
            id = "2",
            eventType = EventTypes.REMOVE_ANIMAL,
            animalId = animal.id,
            payload = RemoveAnimalEvent(animal)
        )

        Mockito.`when`(
            repository.findByEventTypeIn(
                listOf(
                    EventTypes.ADD_ANIMAL,
                    EventTypes.REMOVE_ANIMAL
                )
            )
        ).thenReturn(listOf(addEvent, removeEvent))

        val results: List<AnimalLifespanStats> = service.getAnimalLifespans()

        assertEquals(1, results.size)
        val lifespan = results[0]
        assertEquals(animal.id, lifespan.id)
        assertEquals(animal.name, lifespan.name)
        assertEquals(animal.species, lifespan.species)
        assertEquals(2, lifespan.daysInSystem)
    }

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
        val animalId = 123L
        val addAnimalEvent = AddAnimalEvent()
        val animal = Animal.builder()
            .id(animalId)
            .name("Bobik")
            .species("Dog").build()
        addAnimalEvent.created = date
        val event = AnimalEventDocument(
            id = "1",
            animalId = 100,
            eventType = EventTypes.ADD_ANIMAL,
            payload = addAnimalEvent,
            receivedAt = LocalDateTime.now()
        )
        whenever(repository.findByEventType(EventTypes.ADD_ANIMAL)).thenReturn(listOf(event))

        val result = service.getAddedAnimalsGroupedByDate()

        assertEquals(mapOf(date to 1L), result)
    }

    @Test
    fun `getAnimalLifespans calculates correct lifespan`() {
        val addedDate = LocalDate.of(2025, 5, 1)
        val removedDate = LocalDate.of(2025, 5, 5)
        val animalId = 123L
        val addAnimalEvent = AddAnimalEvent()
        val removeAnimalEvent = RemoveAnimalEvent()
        val animal = Animal.builder()
            .id(animalId)
            .name("Barsik")
            .species("Cat").build()
        addAnimalEvent.animal = animal
        addAnimalEvent.created = addedDate
        removeAnimalEvent.animal = animal
        removeAnimalEvent.created = removedDate
        val addEvent = AnimalEventDocument(
            id = "1",
            animalId = animalId,
            eventType = EventTypes.ADD_ANIMAL,
            payload = addAnimalEvent,
            receivedAt = LocalDateTime.now()
        )

        val removeEvent = AnimalEventDocument(
            id = "2",
            animalId = animalId,
            eventType = EventTypes.REMOVE_ANIMAL,
            payload = removeAnimalEvent,
            receivedAt = LocalDateTime.now()
        )

        whenever(repository.findByEventTypeIn(listOf(EventTypes.ADD_ANIMAL, EventTypes.REMOVE_ANIMAL)))
            .thenReturn(listOf(addEvent, removeEvent))

        val result = service.getAnimalLifespans()

        assertEquals(1, result.size)
        val lifespan = result.first()
        assertEquals(animalId, lifespan.id)
        assertEquals("Barsik", lifespan.name)
        assertEquals("Cat", lifespan.species)
        assertEquals(4, lifespan.daysInSystem)
    }
}
