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
}
