package com.ansh.stats.service.stats

import com.ansh.stats.dto.SubscriptionActionType
import com.ansh.stats.dto.TopicRequestStats
import com.ansh.stats.repository.SubscriptionDecisionStatsRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class SubscriptionRequestStatsServiceTest {

    private val repository: SubscriptionDecisionStatsRepository = mock()
    private val service = SubscriptionRequestStatsService(repository)

    private fun buildStats(
        topic: String,
        approver: String,
        count: Long
    ): TopicRequestStats =
        TopicRequestStats(topic = topic, approver = approver, count = count)

    @Test
    fun `getCount returns correct request count`() {
        whenever(repository.countByActionType(SubscriptionActionType.REQUEST)).thenReturn(5L)

        val result = service.getCount()

        assertEquals(5L, result)
        verify(repository).countByActionType(SubscriptionActionType.REQUEST)
    }

    @Test
    fun `getStatsByTopic returns grouped topic counts`() {
        val aggregationResult = listOf(
            buildStats("animal-info", "", 2),
            buildStats("vaccination-news", "", 1)
        )

        whenever(repository.aggregateRequestsByTopic(SubscriptionActionType.REQUEST.name))
            .thenReturn(aggregationResult)

        val result: List<TopicRequestStats> = service.getStatsByTopic()

        assertEquals(2, result.size)

        val animalInfo = result.find { it.topic == "animal-info" }
        val vaccinationNews = result.find { it.topic == "vaccination-news" }

        assertEquals(2, animalInfo?.count)
        assertEquals(1, vaccinationNews?.count)
    }

    @Test
    fun `getStatsByApprover returns grouped approver counts`() {
        val aggregationResult = listOf(
            buildStats("animal-topic", "doctor1@example.com", 3),
            buildStats("animal-topic", "admin@example.com", 2)
        )

        whenever(repository.aggregateRequestsByApprover(SubscriptionActionType.REQUEST.name))
            .thenReturn(aggregationResult)

        val result: List<TopicRequestStats> = service.getStatsByApprover()

        assertEquals(2, result.size)

        val doctor = result.find { it.approver == "doctor1@example.com" }
        val admin = result.find { it.approver == "admin@example.com" }

        assertEquals(3, doctor?.count)
        assertEquals(2, admin?.count)
    }
}
