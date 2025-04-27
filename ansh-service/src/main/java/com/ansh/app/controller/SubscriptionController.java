package com.ansh.app.controller;

import com.ansh.app.facade.SubscriptionFacade;
import com.ansh.app.service.exception.user.UnauthorizedActionException;
import com.ansh.dto.SubscriptionRequest;
import com.ansh.entity.account.UserProfile.AnimalInfoNotifStatus;
import com.ansh.entity.subscription.Subscription;
import com.ansh.notification.strategy.PendingSubscriptionServiceStrategy;
import com.ansh.repository.entity.PendingSubscriber;
import io.micrometer.common.util.StringUtils;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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

  @Autowired
  private PendingSubscriptionServiceStrategy pendingSubscriptionServiceStrategy;

  @PostMapping("/animal-notify-approve-subscriber")
  public void approve(@RequestBody SubscriptionRequest req) throws UnauthorizedActionException {
    if (StringUtils.isEmpty(req.getApprover()) || StringUtils.isEmpty(req.getEmail()) ||
        StringUtils.isEmpty(req.getTopic())) {
      return;
    }
    var service = pendingSubscriptionServiceStrategy.getServiceByTopic(req.getTopic())
        .orElseThrow(() -> new IllegalArgumentException(
            STR."No service found for topic: \{req.getTopic()}"));

    service.approveSubscriber(req.getEmail(), req.getApprover());
  }

  @PostMapping("/animal-notify-reject-subscriber")
  public void reject(@RequestBody SubscriptionRequest req) throws UnauthorizedActionException {
    if (StringUtils.isEmpty(req.getApprover()) || StringUtils.isEmpty(req.getEmail()) ||
        StringUtils.isEmpty(req.getTopic())) {
      return;
    }

    var service = pendingSubscriptionServiceStrategy.getServiceByTopic(req.getTopic())
        .orElseThrow(() -> new IllegalArgumentException(
            STR."No service found for topic: \{req.getTopic()}"));

    service.rejectSubscriber(req.getEmail(), req.getApprover());
  }

  @PostMapping(value = "/animal-notify-pending-subscribers")
  public List<PendingSubscriber> getPendingSubscribers(@RequestBody SubscriptionRequest req) {
    if (StringUtils.isEmpty(req.getApprover())) {
      return Collections.emptyList();
    }
    return pendingSubscriptionServiceStrategy.getAllServices().stream()
        .flatMap(service -> service.getSubscribersByApprover(req.getApprover()).stream())
        .toList();
  }

  @GetMapping("/animal-notify-pending-no-approver-subscribers")
  public List<PendingSubscriber> getPendingNoApproverSubscribers() {
    return pendingSubscriptionServiceStrategy.getAllServices().stream()
        .flatMap(service -> service.getPendingSubscribersWithoutApprover().stream())
        .toList();
  }

  @PostMapping("/animal-notify-all-approver-subscriptions")
  public DeferredResult<List<Subscription>> getSubscribers(
      @RequestBody SubscriptionRequest subscriptionRequest) {
    return subscriptionFacade.getAllSubscribers(subscriptionRequest);
  }

  @PostMapping("/animal-notify-approver-status")
  public DeferredResult<AnimalInfoNotifStatus> getApproverStatus(
      @RequestBody SubscriptionRequest subscriptionRequest) {
    return subscriptionFacade.getApproverStatus(subscriptionRequest);
  }
}
