package com.ansh.controller;

import static java.lang.StringTemplate.STR;

import com.ansh.dto.SubscriptionRequest;
import com.ansh.entity.account.UserProfile.AnimalInfoNotifStatus;
import com.ansh.entity.subscription.Subscription;
import com.ansh.service.AnimalTopicSubscriberRegistryService;
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
  private AnimalTopicSubscriberRegistryService animalTopicSubscriberRegistryService;

  @PostMapping("/external/animal-notify-subscribe")
  public void subscribe(@RequestBody SubscriptionRequest request) {
    String email = request.getEmail();
    if (email == null || email.isEmpty()) return;
    email = email.replace("\"", "");

    String approver = request.getApprover();
    if (approver == null) approver = "";
    approver = approver.replace("\"", "");
    animalTopicSubscriberRegistryService.registerSubscriber(email, approver);
  }

  @GetMapping("/external/animal-notify-unsubscribe/{token}")
  public String unsubscribe(@PathVariable String token) {
    animalTopicSubscriberRegistryService.unregisterSubscriber(token);
    return STR."Subscription is removed";
  }

  @GetMapping("/external/animal-notify-subscribe-check/{token}")
  public String checkSubscription(@PathVariable String token) {
    boolean isAccepted = animalTopicSubscriberRegistryService.acceptSubscription(token);
    return STR."Your subscription is \{isAccepted ? "accepted" : "not accepted"}";
  }

  @PostMapping("/internal/animal-notify-all-approver-subscriptions")
  public List<Subscription> allSubscriptionsByApprover(@RequestBody SubscriptionRequest request) {
    return animalTopicSubscriberRegistryService.getAllSubscriptions(request.getApprover());
  }

  @PostMapping("/internal/animal-notify-approver-status")
  public AnimalInfoNotifStatus getStatusByApprover(
      @RequestBody SubscriptionRequest request) {
    return animalTopicSubscriberRegistryService.getSubscriptionStatus(request.getApprover());
  }
}
