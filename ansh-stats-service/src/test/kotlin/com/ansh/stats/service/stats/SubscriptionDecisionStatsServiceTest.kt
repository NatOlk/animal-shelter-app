package com.ansh.stats.service.stats

import com.ansh.stats.dto.SubscriptionActionType
import com.ansh.stats.dto.TopicDecisionStats
import com.ansh.stats.repository.SubscriptionDecisionStatsRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*

class SubscriptionDecisionStatsServiceTest {

    private val repository: SubscriptionDecisionStatsRepository = mock()
    private val service = SubscriptionDecisionStatsService(repository)

    private fun buildStat(topic: String, approved: Long, rejected: Long, total: Long): TopicDecisionStats =
        TopicDecisionStats(
            topic = topic,
            approvedCount = approved,
            rejectedCount = rejected,
            count = total
        )

    @Test
    fun `getCount returns correct count from repository`() {
        whenever(repository.countByActionType(SubscriptionActionType.DECISION)).thenReturn(10L)

        val result = service.getCount()

        assertEquals(10L, result)
        verify(repository).countByActionType(SubscriptionActionType.DECISION)
    }

    @Test
    fun `getStatsByTopic returns correct aggregated results`() {
        val aggregationResult = listOf(
            buildStat("animal-info", approved = 1, rejected = 1, total = 2),
            buildStat("animal-news", approved = 2, rejected = 0, total = 2)
        )

        whenever(repository.aggregateDecisionsByTopic(SubscriptionActionType.DECISION.name))
            .thenReturn(aggregationResult)

        val result = service.getStatsByTopic()

        assertEquals(2, result.size)

        val animalInfo = result.find { it.topic == "animal-info" }!!
        assertEquals(1, animalInfo.approvedCount)
        assertEquals(1, animalInfo.rejectedCount)
        assertEquals(2, animalInfo.count)

        val animalNews = result.find { it.topic == "animal-news" }!!
        assertEquals(2, animalNews.approvedCount)
        assertEquals(0, animalNews.rejectedCount)
        assertEquals(2, animalNews.count)
    }
}