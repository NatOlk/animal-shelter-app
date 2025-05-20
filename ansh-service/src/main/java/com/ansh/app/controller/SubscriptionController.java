package com.ansh.app.controller;

import com.ansh.app.facade.SubscriptionFacade;
import com.ansh.dto.NotificationStatusDTO;
import com.ansh.dto.SubscriptionRequest;
import com.ansh.entity.subscription.Subscription;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/subscription")
@Tag(name = "Subscriptions", description = "Endpoints for registering, unsubscribing and managing subscriptions")
@SecurityRequirement(name = "bearerAuth")
public class SubscriptionController {

  @Autowired
  private SubscriptionFacade subscriptionFacade;

  @Operation(
      summary = "Register a new subscriber",
      description = "Registers a user for a specific notification topic. Requires valid approver or token."
  )
  @PostMapping("/register")
  public Mono<Boolean> registerSubscriber(@RequestBody SubscriptionRequest req) {
    return subscriptionFacade.registerSubscription(req);
  }

  @Operation(
      summary = "Unsubscribe user",
      description = "Unsubscribes the user from a specific notification topic."
  )
  @PostMapping("/unsubscribe")
  public Mono<Boolean> unsubscribe(@RequestBody SubscriptionRequest req) {
    return subscriptionFacade.unsubscribe(req);
  }

  @Operation(
      summary = "Get all subscribers for an approver",
      description = "Returns a list of all current subscribers associated with the specified approver."
  )
  @PostMapping("/all")
  public DeferredResult<List<Subscription>> getSubscribers(@RequestBody SubscriptionRequest req) {
    return subscriptionFacade.getAllSubscribers(req.getApprover());
  }

  @Operation(
      summary = "Get notification subscription statuses",
      description = "Returns the current notification subscription status for each topic for the given account."
  )
  @PostMapping("/statuses")
  public DeferredResult<NotificationStatusDTO> getStatuses(@RequestBody SubscriptionRequest req) {
    return subscriptionFacade.getNotificationStatusesByAccount(req.getApprover());
  }
}