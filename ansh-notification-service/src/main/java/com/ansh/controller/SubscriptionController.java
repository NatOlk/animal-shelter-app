package com.ansh.controller;

import com.ansh.dto.SubscriptionRequest;
import com.ansh.entity.animal.UserProfile;
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
    String email = request.getEmail().replace("\"", "");
    String approver = request.getApprover().replace("\"", "");
    animalTopicSubscriberRegistryService.registerSubscriber(email, approver);
  }

  @GetMapping("/external/animal-notify-unsubscribe/{token}")
  public String unsubscribe(@PathVariable String token) {
    animalTopicSubscriberRegistryService.unregisterSubscriber(token);
    return "Subscription with token " + token + " removed";
  }

  @GetMapping("/external/animal-notify-subscribe-check/{token}")
  public String checkSubscription(@PathVariable String token) {
    boolean isAccepted = animalTopicSubscriberRegistryService.acceptSubscription(token);
    return "Subscription with token " + token + " is " + (isAccepted ? "valid" : "invalid");
  }

  @PostMapping("/internal/animal-notify-all-approver-subscriptions")
  public List<Subscription> allSubscriptionsByApprover(@RequestBody SubscriptionRequest request) {
    return animalTopicSubscriberRegistryService.getAllSubscriptions(request.getApprover());
  }

  @PostMapping("/internal/animal-notify-approver-status")
  public UserProfile.AnimalNotificationSubscriptionStatus getStatusByApprover(
      @RequestBody SubscriptionRequest request) {
    return animalTopicSubscriberRegistryService.getStatusByApprover(request.getApprover());
  }
}
