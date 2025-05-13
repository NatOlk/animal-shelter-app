package com.ansh.stats.service.stats

import com.ansh.event.subscription.SubscriptionDecisionEvent
import com.ansh.stats.dto.SubscriptionActionType
import com.ansh.stats.dto.TopicRequestStats
import com.ansh.stats.entity.SubscriptionDecisionEventDocument
import com.ansh.stats.repository.SubscriptionDecisionStatsRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.LocalDate

class SubscriptionRequestStatsServiceTest {

    private val repository: SubscriptionDecisionStatsRepository = mock()
    private val service = SubscriptionRequestStatsService(repository)

    private fun buildRequestEvent(topic: String): SubscriptionDecisionEventDocument {
        val payload = SubscriptionDecisionEvent.builder()
            .email("user@example.com")
            .approver("admin@example.com")
            .topic(topic)
            .reject(false)
            .created(LocalDate.now()).build()
        return SubscriptionDecisionEventDocument(
            payload = payload,
            actionType = SubscriptionActionType.REQUEST
        )
    }

    @Test
    fun `getCount returns correct request count`() {
        whenever(repository.countByActionType(SubscriptionActionType.REQUEST)).thenReturn(5L)

        val result = service.getCount()

        assertEquals(5L, result)
        verify(repository).countByActionType(SubscriptionActionType.REQUEST)
    }

    @Test
    fun `getStatsByTopic returns grouped topic counts`() {
        val events = listOf(
            buildRequestEvent("animal-info"),
            buildRequestEvent("animal-info"),
            buildRequestEvent("vaccination-news")
        )

        whenever(repository.findAllByActionType(SubscriptionActionType.REQUEST)).thenReturn(events)

        val result: List<TopicRequestStats> = service.getStatsByTopic()

        assertEquals(2, result.size)

        val animalInfo = result.find { it.topic == "animal-info" }
        val vaccinationNews = result.find { it.topic == "vaccination-news" }

        assertEquals(2, animalInfo?.count)
        assertEquals(1, vaccinationNews?.count)
    }

    @Test
    fun `getStatsByTopic should nor returns grouped topic counts if DECISION type`() {
        val events = listOf(
            buildRequestEvent("animal-info"),
            buildRequestEvent("animal-info"),
            buildRequestEvent("vaccination-news")
        )

        whenever(repository.findAllByActionType(SubscriptionActionType.DECISION)).thenReturn(events)

        val result: List<TopicRequestStats> = service.getStatsByTopic()

        assertEquals(0, result.size)
    }

    @Test
    fun `getStatsByTopic should returns grouped topic counts only for REQUEST TYPE`() {
        val events = listOf(
            buildRequestEvent("animal-info"),
            buildRequestEvent("animal-info")
        )

        val events2 = listOf(
            buildRequestEvent("vaccination-news")
        )

        whenever(repository.findAllByActionType(SubscriptionActionType.REQUEST)).thenReturn(events)

        whenever(repository.findAllByActionType(SubscriptionActionType.DECISION)).thenReturn(events2)

        val result: List<TopicRequestStats> = service.getStatsByTopic()

        assertEquals(1, result.size)

        val animalInfo = result.find { it.topic == "animal-info" }
        val vaccinationNews = result.find { it.topic == "vaccination-news" }

        assertEquals(2, animalInfo?.count)
        assertEquals(null, vaccinationNews)
    }
}
