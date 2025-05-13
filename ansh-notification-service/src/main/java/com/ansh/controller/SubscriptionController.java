package com.ansh.controller;

import com.ansh.dto.SubscriptionRequest;
import com.ansh.dto.NotificationStatusDTO;
import com.ansh.entity.subscription.Subscription;
import com.ansh.facade.SubscriptionFacade;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SubscriptionController {

  @Autowired
  private SubscriptionFacade subscriptionFacade;

  @PostMapping("/internal/subscription/all")
  public List<Subscription> allSubscriptionsByAccount(@RequestBody SubscriptionRequest request) {
    return subscriptionFacade.getAllSubscriptionByAccount(request.getApprover());
  }

  @PostMapping("/internal/subscription/statuses")
  public NotificationStatusDTO getStatusesByAccount(@RequestBody SubscriptionRequest request) {
    return subscriptionFacade.getSubscriptionStatuses(request.getApprover());
  }

  @PostMapping("/internal/subscription/register")
  public void registerEmployeeSubscription(@RequestBody SubscriptionRequest request) {
    subscriptionFacade.registerSubscription(request);
  }

  @PostMapping("/internal/subscription/unsubscribe")
  public String unsubscribe(@RequestBody SubscriptionRequest request) {
    subscriptionFacade.unsubscribe(request);
    return "Subscription is removed";
  }
}
