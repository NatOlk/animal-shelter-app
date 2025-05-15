package com.ansh.stats.controller

import com.ansh.stats.dto.TopicDecisionStats
import com.ansh.stats.service.stats.SubscriptionDecisionStatsService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/stats")
@Tag(name = "Subscription Decisions", description = "Statistics about approved/rejected subscriptions")
class SubscriptionDecisionStatsController(
    private val service: SubscriptionDecisionStatsService
) {

    @GetMapping("/subscription/decision/count")
    @Operation(
        summary = "Get total number of subscription decisions",
        description = "Returns the total number of decisions (approved or rejected) made for subscriptions.",
        responses = [ApiResponse(responseCode = "200", description = "Total count of subscription decisions")]
    )
    fun getSubscriptionDecisionCount(): Long = service.getCount()

    @GetMapping("/subscription/decision/by-topic")
    @Operation(
        summary = "Get subscription decisions grouped by topic",
        description = "Returns statistics of approved and rejected subscriptions grouped by topic.",
        responses = [ApiResponse(responseCode = "200", description = "List of decision stats per topic")]
    )
    fun getStatsByTopic(): List<TopicDecisionStats> = service.getStatsByTopic()
}