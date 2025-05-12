package com.ansh.stats.constants

import com.ansh.event.animal.AddAnimalEvent
import com.ansh.event.animal.RemoveAnimalEvent
import com.ansh.event.vaccination.AddVaccinationEvent
import com.ansh.event.vaccination.RemoveVaccinationEvent

object EventTypes {
    val ADD_ANIMAL = AddAnimalEvent::class.qualifiedName!!
    val REMOVE_ANIMAL = RemoveAnimalEvent::class.qualifiedName!!
    val ADD_VACCINATION = AddVaccinationEvent::class.qualifiedName!!
    val REMOVE_VACCINATION = RemoveVaccinationEvent::class.qualifiedName!!
}
