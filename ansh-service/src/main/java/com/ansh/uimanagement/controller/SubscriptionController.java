package com.ansh.uimanagement.controller;

import com.ansh.dto.SubscriptionRequest;
import com.ansh.entity.animal.UserProfile.AnimalNotificationSubscriptionStatus;
import com.ansh.entity.subscription.Subscription;
import com.ansh.repository.entity.PendingSubscriber;
import com.ansh.uimanagement.service.AnimalTopicSubscriptionService;
import com.ansh.uimanagement.service.SubscriptionService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SubscriptionController {

  @Autowired
  private SubscriptionService subscriptionService;
  @Autowired
  private AnimalTopicSubscriptionService animalTopicSubscriptionService;

  @PostMapping("/animal-notify-approve-subscriber")
  public void approve(@RequestBody SubscriptionRequest subscriptionRequest) {
    animalTopicSubscriptionService.approveSubscriber(subscriptionRequest.getEmail(),
        subscriptionRequest.getApprover());
  }

  @PostMapping("/animal-notify-reject-subscriber")
  public void reject(@RequestBody SubscriptionRequest subscriptionRequest) {
    animalTopicSubscriptionService.rejectSubscriber(subscriptionRequest.getEmail());
  }

  @PostMapping(value = "/animal-notify-pending-subscribers")
  public List<PendingSubscriber> getPendingSubscribers(
      @RequestBody SubscriptionRequest subscriptionRequest) {
    return subscriptionService.getPendingSubscribers(subscriptionRequest.getApprover());
  }

  @GetMapping("/animal-notify-pending-no-approver-subscribers")
  public List<PendingSubscriber> getPendingNoApproverSubscribers() {
    return subscriptionService.getPendingNoApproverSubscribers();
  }

  @PostMapping("/animal-notify-all-approver-subscriptions")
  public List<Subscription> getSubscribers(@RequestBody SubscriptionRequest subscriptionRequest) {
    return subscriptionService.getAllSubscriptionByApprover(subscriptionRequest.getApprover());
  }

  @PostMapping("/animal-notify-approver-status")
  public AnimalNotificationSubscriptionStatus getApproverStatus(
      @RequestBody SubscriptionRequest subscriptionRequest) {
    return subscriptionService.getStatusByApprover(subscriptionRequest.getApprover());
  }
}
