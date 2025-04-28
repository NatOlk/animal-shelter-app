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
  private SubscriptionFacade subscriptionFacade;

  @PostMapping("/internal/animal-notify-all-approver-subscriptions")
  public List<Subscription> allSubscriptionsByApprover(@RequestBody SubscriptionRequest request) {
    return subscriptionFacade.getAllSubscriptionByApprover(request.getApprover());
  }

  @PostMapping("/internal/animal-notify-approver-status")
  public AnimalInfoNotifStatus getStatusByApprover(
      @RequestBody SubscriptionRequest request) {
    // return animalShelterNewsSubscriber.getSubscriptionStatus(request.getApprover());
    return null;
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
