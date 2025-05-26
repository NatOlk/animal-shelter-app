package com.ansh.stats.utils

import com.ansh.event.subscription.SubscriptionDecisionEvent
import com.ansh.stats.dto.SubscriptionActionType
import com.ansh.stats.entity.SubscriptionDecisionEventDocument

fun SubscriptionDecisionEvent.toDocument(type: SubscriptionActionType): SubscriptionDecisionEventDocument {
    return SubscriptionDecisionEventDocument(
        payload = this,
        actionType = type
    )
}