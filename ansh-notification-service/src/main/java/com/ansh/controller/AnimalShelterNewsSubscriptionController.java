package com.ansh.controller;

import com.ansh.dto.SubscriptionRequest;
import com.ansh.service.SubscriberRegistryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnimalShelterNewsSubscriptionController {

  @Autowired
  @Qualifier("animalShelterNewsSubscriber")
  private SubscriberRegistryService animalShelterNewsSubscriber;

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
}
