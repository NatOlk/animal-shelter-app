package com.ansh.controller;

import com.ansh.dto.SubscriptionRequest;
import com.ansh.entity.account.UserProfile.AnimalInfoNotifStatus;
import com.ansh.entity.subscription.Subscription;
import com.ansh.facade.SubscriptionFacade;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SubscriptionController {

  @Autowired
  private SubscriptionFacade subscriptionFacade;

  @PostMapping("/internal/animal-notify-all-approver-subscriptions")
  public List<Subscription> allSubscriptionsByApprover(@RequestBody SubscriptionRequest request) {
    return subscriptionFacade.getAllSubscriptionByApprover(request.getApprover());
  }

  @PostMapping("/internal/animal-notify-approver-status")
  public AnimalInfoNotifStatus getStatusByApprover(@RequestBody SubscriptionRequest request) {
    return subscriptionFacade.getSubscriptionStatus(request);
  }

  @PostMapping("/external/subscriptions/register")
  public void registerEmployeeSubscription(@RequestBody SubscriptionRequest request) {
    subscriptionFacade.registerEmployeeSubscription(request);
  }

  @GetMapping("/external/subscriptions/unsubscribe/{token}")
  public String unsubscribe(@PathVariable String token) {
    subscriptionFacade.unregisterEmployeeSubscription(token);
    return "Subscription is removed";
  }
}
