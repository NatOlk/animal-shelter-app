package com.ansh.stats.controller

import com.ansh.stats.dto.TopicRequestStats
import com.ansh.stats.service.stats.SubscriptionRequestStatsService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/stats")
@Tag(name = "Subscription Requests", description = "Statistics about subscription requests per topic")
class SubscriptionRequestStatsController(
    private val service: SubscriptionRequestStatsService
) {

    @GetMapping("/subscription/request/count")
    @Operation(
        summary = "Get total number of subscription requests",
        description = "Returns the total number of users who requested subscriptions.",
        responses = [ApiResponse(responseCode = "200", description = "Total count of subscription requests")]
    )
    fun getSubscriptionDecisionCount(): Long = service.getCount()

    @GetMapping("/subscription/request/by-topic")
    @Operation(
        summary = "Get subscription requests grouped by topic",
        description = "Returns the number of subscription requests grouped by topic.",
        responses = [ApiResponse(responseCode = "200", description = "List of request stats per topic")]
    )
    fun getStatsByTopic(): List<TopicRequestStats> = service.getStatsByTopic()

    @GetMapping("/subscription/request/by-approver")
    @Operation(
        summary = "Get subscription requests grouped by approver",
        description = "Returns the number of subscription requests grouped by approver.",
        responses = [ApiResponse(responseCode = "200", description = "List of request stats per approver")]
    )
    fun getStatsByApprover(): List<TopicRequestStats> = service.getStatsByApprover()
}