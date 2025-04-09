package com.ansh.stats.entity


import com.ansh.event.subscription.SubscriptionDecisionEvent
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
@Document(collection = "subscription_decision_events")
data class SubscriptionDecisionEventDocument(
    @Id val id: String? = null,
    val payload: SubscriptionDecisionEvent,
    val receivedAt: LocalDateTime = LocalDateTime.now()
)
