package com.ansh.stats.controller

import com.ansh.stats.entity.TopicDecisionStats
import com.ansh.stats.service.stats.SubscriptionDecisionStatsService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/stats")
class SubscriptionDecisionStatsController(
    private val service: SubscriptionDecisionStatsService
) {

    @GetMapping("/subscription/count")
    fun getSubscriptionDecisionCount(): Long = service.getCount()

    @GetMapping("/subscription/by-topic")
    fun getStatsByTopic(): List<TopicDecisionStats> = service.getStatsByTopic()
}