package com.ansh.stats.utils

import com.ansh.event.AnimalShelterEvent
import com.ansh.stats.entity.AnimalEventDocument

fun AnimalShelterEvent.toDocument(): AnimalEventDocument {
    this.animal.vaccinations?.forEach { it.animal = null }
    return AnimalEventDocument(
        eventType = this.javaClass.name,
        animalId = this.animal.id,
        payload = this
    )
}
