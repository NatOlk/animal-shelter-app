package com.ansh.controller;

import com.ansh.dto.EmployeeSubscriptionRequest;
import com.ansh.dto.SubscriptionRequest;
import com.ansh.entity.account.UserProfile.AnimalInfoNotifStatus;
import com.ansh.entity.subscription.Subscription;
import com.ansh.facade.SubscriptionFacade;
import com.ansh.service.SubscriberRegistryService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SubscriptionController {

  @Autowired
  @Qualifier("animalShelterNewsSubscriber")
  private SubscriberRegistryService animalShelterNewsSubscriber;

  @Autowired
  private SubscriptionFacade subscriptionFacade;

  @PostMapping("/external/animal-notify-subscribe")

  public void subscribe(@RequestBody SubscriptionRequest request) {
    String email = request.getEmail();
    if (email == null || email.isEmpty()) {
      return;
    }
    email = email.replace("\"", "");

    String approver = request.getApprover();
    if (approver == null) {
      approver = "";
    }
    approver = approver.replace("\"", "");
    animalShelterNewsSubscriber.registerSubscriber(email, approver);
  }

  @GetMapping("/external/animal-notify-unsubscribe/{token}")
  public String unsubscribe(@PathVariable String token) {
    animalShelterNewsSubscriber.unregisterSubscriber(token);
    return "Subscription is removed";
  }

  @GetMapping("/external/animal-notify-subscribe-check/{token}")
  public String checkSubscription(@PathVariable String token) {
    boolean isAccepted = animalShelterNewsSubscriber.acceptSubscription(token);
    return STR."Your subscription is \{isAccepted ? "accepted" : "not accepted"}";
  }

  @PostMapping("/internal/animal-notify-all-approver-subscriptions")
  public List<Subscription> allSubscriptionsByApprover(@RequestBody SubscriptionRequest request) {
    return subscriptionFacade.getAllSubscriptionByApprover(request.getApprover());
  }

  @PostMapping("/internal/animal-notify-approver-status")
  public AnimalInfoNotifStatus getStatusByApprover(
      @RequestBody SubscriptionRequest request) {
    return animalShelterNewsSubscriber.getSubscriptionStatus(request.getApprover());
  }

  @PostMapping("/internal/subscriptions/register")
  public ResponseEntity<Void> registerEmployeeSubscription(@RequestBody EmployeeSubscriptionRequest request) {
    /* Subscription subscription = new Subscription();
    subscription.setEmail(request.getEmail());
    subscription.setTopic(request.getTopic());
    subscription.setToken(UUID.randomUUID().toString());
    subscription.setAccepted(true);
    subscription.setApproved(true);
    subscriptionRepository.save(subscription);

    emailService.sendSubscriptionApprovedEmail(subscription);
    return ResponseEntity.ok().build(); */
    return null;
  }
}
