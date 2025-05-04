package com.ansh.stats.service

import com.ansh.entity.animal.Animal
import com.ansh.event.animal.AddAnimalEvent
import com.ansh.event.animal.RemoveAnimalEvent
import com.ansh.stats.entity.AnimalEventDocument
import com.ansh.stats.entity.AnimalLifespanStats
import com.ansh.stats.repository.AnimalEventRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.time.LocalDateTime

class AnimalStatsServiceTest {

    private val repository: AnimalEventRepository = Mockito.mock(AnimalEventRepository::class.java)
    private val service = AnimalStatsService(repository)

    @Test
    fun `getAnimalLifespans returns correct lifespan stats`() {
        val now = LocalDateTime.now()
        val animal = Animal().apply {
            id = 1L
            name = "Bella"
            species = "Dog"
        }

        val addEvent = AnimalEventDocument(
            id = "1",
            eventType = "com.ansh.event.animal.AddAnimalEvent",
            animalId = animal.id,
            payload = AddAnimalEvent(animal),
            receivedAt = now.minusDays(2).minusHours(5).minusMinutes(30)
        )

        val removeEvent = AnimalEventDocument(
            id = "2",
            eventType = "com.ansh.event.animal.RemoveAnimalEvent",
            animalId = animal.id,
            payload = RemoveAnimalEvent(animal),
            receivedAt = now
        )

        Mockito.`when`(
            repository.findByEventTypeIn(
                listOf(
                    "com.ansh.event.animal.AddAnimalEvent",
                    "com.ansh.event.animal.RemoveAnimalEvent"
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
        assertEquals(5, lifespan.hoursInSystem)
        assertEquals(30, lifespan.minutesInSystem)
    }
}
