package com.ansh.app.controller;

import com.ansh.app.facade.SubscriptionFacade;
import com.ansh.dto.NotificationStatusDTO;
import com.ansh.dto.SubscriptionRequest;
import com.ansh.entity.subscription.Subscription;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
@RequestMapping("/api")
public class SubscriptionController {

  @Autowired
  private SubscriptionFacade subscriptionFacade;

  @PostMapping("/subscription/register")
  public void registerSubscriber(@RequestBody SubscriptionRequest req) {
    subscriptionFacade.registerSubscription(req);
  }

  @PostMapping("/subscription/unsubscribe")
  public void unsubscribe(@RequestBody SubscriptionRequest req) {
    subscriptionFacade.unsubscribe(req);
  }

  @PostMapping("/subscription/all")
  public DeferredResult<List<Subscription>> getSubscribers(@RequestBody SubscriptionRequest req) {
    return subscriptionFacade.getAllSubscribers(req.getApprover());
  }

  @PostMapping("/subscription/statuses")
  public DeferredResult<NotificationStatusDTO> getStatuses(@RequestBody SubscriptionRequest req) {
    return subscriptionFacade.getNotificationStatusesByAccount(req.getApprover());
  }
}
