package com.ansh.facade.impl;

import com.ansh.dto.NotificationStatusDTO;
import com.ansh.dto.SubscriptionRequest;
import com.ansh.entity.account.UserProfile.AnimalInfoNotifStatus;
import com.ansh.entity.subscription.Subscription;
import com.ansh.event.AnimalShelterTopic;
import com.ansh.facade.SubscriptionFacade;
import com.ansh.service.SubscriberRegistryService;
import com.ansh.strategy.SubscriberRegistryServiceStrategy;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionFacadeImpl implements SubscriptionFacade {

  @Autowired
  private SubscriberRegistryServiceStrategy subscriberRegistryServiceStrategy;

  @Override
  public List<Subscription> getAllSubscriptionByAccount(String approver) {
    List<Subscription> allSubscriptions = new ArrayList<>();
    for (SubscriberRegistryService service : subscriberRegistryServiceStrategy.getAllServices()) {
      allSubscriptions.addAll(service.getAllSubscriptions(approver));
    }
    return allSubscriptions;
  }

  @Override
  public void registerSubscription(SubscriptionRequest req) {
    getServiceByTopicOrThrow(req.getTopic())
        .registerSubscriber(req.getEmail(), req.getApprover());
  }

  @Override
  public void unsubscribe(String token) {
    subscriberRegistryServiceStrategy.getAllServices()
        .forEach(service -> service.unsubscribe(token));
  }

  @Override
  public void unsubscribe(SubscriptionRequest req) {
    getServiceByTopicOrThrow(req.getTopic())
        .unsubscribe(req.getEmail(), req.getApprover());
  }

  @Override
  public NotificationStatusDTO getSubscriptionStatuses(String account) {
    var animalNewsStatus = getStatusByTopic(account,
        AnimalShelterTopic.ANIMAL_SHELTER_NEWS.getTopicName());
    var animalInfoStatus = getStatusByTopic(account,
        AnimalShelterTopic.ANIMAL_INFO.getTopicName());
    var vaccinationStatus = getStatusByTopic(account,
        AnimalShelterTopic.VACCINATION_INFO.getTopicName());

    return new NotificationStatusDTO(animalNewsStatus, animalInfoStatus, vaccinationStatus);
  }

  @Override
  public void handleSubscriptionApproval(String email, String approver, String topic,
      boolean reject) {
    getServiceByTopicOrThrow(topic)
        .handleSubscriptionApproval(email, approver, reject);
  }

  private SubscriberRegistryService getServiceByTopicOrThrow(String topic) {
    return subscriberRegistryServiceStrategy.getServiceByTopic(topic)
        .orElseThrow(
            () -> new IllegalArgumentException(STR."No service found for topic: \{topic}"));
  }

  private AnimalInfoNotifStatus getStatusByTopic(String account, String topic) {
    return subscriberRegistryServiceStrategy
        .getServiceByTopic(topic)
        .map(service -> service.getSubscriptionStatus(account))
        .orElse(AnimalInfoNotifStatus.NONE);
  }
}
