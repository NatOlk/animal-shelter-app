package com.ansh.stats.service

import com.ansh.event.subscription.SubscriptionDecisionEvent
import com.ansh.stats.dto.SubscriptionActionType
import com.ansh.stats.entity.SubscriptionDecisionEventDocument
import com.ansh.stats.repository.SubscriptionDecisionStatsRepository
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import java.time.LocalDate

class SubscriptionDecisionServiceTest {

    private val repository: SubscriptionDecisionStatsRepository = mock()
    private val service = SubscriptionDecisionService(repository)

    private val testEvent = SubscriptionDecisionEvent.builder()
        .email("test@example.com")
        .eventId("1-1-1")
        .approver("admin@example.com")
        .topic("animal-news")
        .reject(false)
        .created(LocalDate.now())
        .build()

    @Test
    fun `should save decision event with type DECISION`() {
        service.saveDecisionEvent(testEvent)

        argumentCaptor<SubscriptionDecisionEventDocument>().apply {
            verify(repository).save(capture())

            val saved = firstValue
            assert(saved.payload == testEvent)
            assert(saved.actionType == SubscriptionActionType.DECISION)
        }
    }

    @Test
    fun `should save request event with type REQUEST`() {
        service.saveRequestEvent(testEvent)

        argumentCaptor<SubscriptionDecisionEventDocument>().apply {
            verify(repository).save(capture())

            val saved = firstValue
            assert(saved.payload == testEvent)
            assert(saved.actionType == SubscriptionActionType.REQUEST)
        }
    }
}
