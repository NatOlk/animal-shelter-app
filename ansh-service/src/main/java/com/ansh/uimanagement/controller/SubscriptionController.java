package com.ansh.uimanagement.controller;

import com.ansh.auth.service.UserProfileService;
import com.ansh.dto.SubscriptionRequest;
import com.ansh.entity.animal.UserProfile.AnimalNotificationSubscriptionStatus;
import com.ansh.entity.subscription.Subscription;
import com.ansh.repository.entity.PendingSubscriber;
import com.ansh.uimanagement.service.AnimalTopicSubscriptionService;
import com.ansh.uimanagement.service.SubscriptionService;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.util.StringUtils;

@RestController
public class SubscriptionController {

  @Autowired
  private SubscriptionService subscriptionService;
  @Autowired
  private AnimalTopicSubscriptionService animalTopicSubscriptionService;
  @Autowired
  private UserProfileService userProfileService;

  @PostMapping("/animal-notify-approve-subscriber")
  public void approve(@RequestBody SubscriptionRequest subscriptionRequest) {
    if (StringUtils.isEmpty(subscriptionRequest.getApprover()) || StringUtils.isEmpty(
        subscriptionRequest.getEmail())) {
      return;
    }
    animalTopicSubscriptionService.approveSubscriber(subscriptionRequest.getEmail(),
        subscriptionRequest.getApprover());
  }

  @PostMapping("/animal-notify-reject-subscriber")
  public void reject(@RequestBody SubscriptionRequest subscriptionRequest) {
    if (StringUtils.isEmpty(subscriptionRequest.getEmail())) {
      return;
    }
    animalTopicSubscriptionService.rejectSubscriber(subscriptionRequest.getEmail());
  }

  @PostMapping(value = "/animal-notify-pending-subscribers")
  public List<PendingSubscriber> getPendingSubscribers(
      @RequestBody SubscriptionRequest subscriptionRequest) {
    if (StringUtils.isEmpty(subscriptionRequest.getApprover())) {
      return Collections.emptyList();
    }
    return subscriptionService.getPendingSubscribers(subscriptionRequest.getApprover());
  }

  @GetMapping("/animal-notify-pending-no-approver-subscribers")
  public List<PendingSubscriber> getPendingNoApproverSubscribers() {
    return subscriptionService.getPendingNoApproverSubscribers();
  }

  @PostMapping("/animal-notify-all-approver-subscriptions")
  public List<Subscription> getSubscribers(@RequestBody SubscriptionRequest subscriptionRequest) {
    if (StringUtils.isEmpty(subscriptionRequest.getApprover())) return Collections.emptyList();
    return subscriptionService.getAllSubscriptionByApprover(subscriptionRequest.getApprover());
  }

  @PostMapping("/animal-notify-approver-status")
  public AnimalNotificationSubscriptionStatus getApproverStatus(
      @RequestBody SubscriptionRequest subscriptionRequest) {
    if (StringUtils.isEmpty(subscriptionRequest.getApprover())) return null;

    AnimalNotificationSubscriptionStatus status = subscriptionService.getStatusByApprover(
        subscriptionRequest.getApprover());
    userProfileService.updateNotificationStatusOfAuthUser(status);
    return status;
  }
}
