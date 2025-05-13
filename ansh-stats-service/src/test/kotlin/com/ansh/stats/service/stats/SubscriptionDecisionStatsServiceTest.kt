package com.ansh.stats.service.stats

import com.ansh.event.subscription.SubscriptionDecisionEvent
import com.ansh.stats.dto.SubscriptionActionType
import com.ansh.stats.entity.SubscriptionDecisionEventDocument
import com.ansh.stats.repository.SubscriptionDecisionStatsRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import java.time.LocalDate

class SubscriptionDecisionStatsServiceTest {

    private val repository: SubscriptionDecisionStatsRepository = mock()
    private val service = SubscriptionDecisionStatsService(repository)

    private fun createEvent(topic: String, reject: Boolean): SubscriptionDecisionEventDocument {
        val payload = SubscriptionDecisionEvent.builder()
            .email("user@example.com")
            .approver("admin@example.com")
            .topic(topic)
            .reject(reject)
            .created(LocalDate.now()).build()

        return SubscriptionDecisionEventDocument(
            payload = payload,
            actionType = SubscriptionActionType.DECISION
        )
    }

    @Test
    fun `getCount returns correct count from repository`() {
        whenever(repository.countByActionType(SubscriptionActionType.DECISION)).thenReturn(10L)

        val result = service.getCount()

        assertEquals(10L, result)
        verify(repository).countByActionType(SubscriptionActionType.DECISION)
    }

    @Test
    fun `getStatsByTopic groups and counts correctly`() {
        val events = listOf(
            createEvent("animal-info", reject = false),
            createEvent("animal-info", reject = true),
            createEvent("animal-news", reject = false),
            createEvent("animal-news", reject = false)
        )

        whenever(repository.findAllByActionType(SubscriptionActionType.DECISION)).thenReturn(events)

        val stats = service.getStatsByTopic()

        assertEquals(2, stats.size)

        val animalInfo = stats.find { it.topic == "animal-info" }!!
        assertEquals(1, animalInfo.approvedCount)
        assertEquals(1, animalInfo.rejectedCount)
        assertEquals(2, animalInfo.count)

        val animalNews = stats.find { it.topic == "animal-news" }!!
        assertEquals(2, animalNews.approvedCount)
        assertEquals(0, animalNews.rejectedCount)
        assertEquals(2, animalNews.count)
    }
}
