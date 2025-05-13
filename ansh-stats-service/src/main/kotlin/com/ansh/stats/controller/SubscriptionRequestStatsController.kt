package com.ansh.stats.controller

import com.ansh.stats.dto.TopicRequestStats
import com.ansh.stats.service.stats.SubscriptionRequestStatsService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/stats")
class SubscriptionRequestStatsController(
    private val service: SubscriptionRequestStatsService
) {
    @GetMapping("/subscription/request/count")
    fun getSubscriptionDecisionCount(): Long = service.getCount()

    @GetMapping("/subscription/request/by-topic")
    fun getStatsByTopic(): List<TopicRequestStats> = service.getStatsByTopic()
}