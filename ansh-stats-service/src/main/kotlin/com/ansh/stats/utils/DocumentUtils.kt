package com.ansh.stats.utils

import com.ansh.event.AnimalShelterEvent
import com.ansh.event.subscription.SubscriptionDecisionEvent
import com.ansh.stats.dto.SubscriptionActionType
import com.ansh.stats.entity.AnimalEventDocument
import com.ansh.stats.entity.SubscriptionDecisionEventDocument

fun AnimalShelterEvent.toDocument(): AnimalEventDocument {
    this.animal.vaccinations?.forEach { it.animal = null }
    return AnimalEventDocument(
        eventType = this.javaClass.name,
        animalId = this.animal.id,
        eventId = this.eventId,
        payload = this
    )
}

fun SubscriptionDecisionEvent.toDocument(type: SubscriptionActionType): SubscriptionDecisionEventDocument {
    return SubscriptionDecisionEventDocument(
        payload = this,
        eventId = this.eventId,
        actionType = type
    )
}
