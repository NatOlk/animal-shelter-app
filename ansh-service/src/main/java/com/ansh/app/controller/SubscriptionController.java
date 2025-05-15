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

@RestController
@RequestMapping("/api")
@Tag(name = "Subscriptions", description = "Endpoints for registering, unsubscribing and managing subscriptions")
@SecurityRequirement(name = "bearerAuth") // требует авторизацию по JWT
public class SubscriptionController {

  @Autowired
  private SubscriptionFacade subscriptionFacade;

  @Operation(
      summary = "Register a new subscriber",
      description = "Registers a user for a specific notification topic. Requires valid approver or token."
  )
  @PostMapping("/subscription/register")
  public void registerSubscriber(@RequestBody SubscriptionRequest req) {
    subscriptionFacade.registerSubscription(req);
  }

  @Operation(
      summary = "Unsubscribe user",
      description = "Unsubscribes the user from a specific notification topic."
  )
  @PostMapping("/subscription/unsubscribe")
  public void unsubscribe(@RequestBody SubscriptionRequest req) {
    subscriptionFacade.unsubscribe(req);
  }

  @Operation(
      summary = "Get all subscribers for an approver",
      description = "Returns a list of all current subscribers associated with the specified approver."
  )
  @PostMapping("/subscription/all")
  public DeferredResult<List<Subscription>> getSubscribers(@RequestBody SubscriptionRequest req) {
    return subscriptionFacade.getAllSubscribers(req.getApprover());
  }

  @Operation(
      summary = "Get notification subscription statuses",
      description = "Returns the current notification subscription status for each topic for the given account."
  )
  @PostMapping("/subscription/statuses")
  public DeferredResult<NotificationStatusDTO> getStatuses(@RequestBody SubscriptionRequest req) {
    return subscriptionFacade.getNotificationStatusesByAccount(req.getApprover());
  }
}