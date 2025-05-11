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
    subscriberRegistryServiceStrategy.getServiceByTopic(req.getTopic())
        .ifPresent(service -> service.registerSubscriber(req.getEmail(), req.getApprover()));
  }

  @Override
  public void unsubscribe(String token) {
    subscriberRegistryServiceStrategy.getAllServices()
        .forEach(service -> service.unsubscribe(token));
  }

  @Override
  public void unsubscribe(SubscriptionRequest req) {
    subscriberRegistryServiceStrategy.getServiceByTopic(req.getTopic())
        .ifPresent(service -> service.unsubscribe(req.getEmail(), req.getApprover()));
  }

  @Override
  public NotificationStatusDTO getSubscriptionStatuses(String account) {

    var animalNewsStatus = subscriberRegistryServiceStrategy
        .getServiceByTopic(AnimalShelterTopic.ANIMAL_SHELTER_NEWS.getTopicName())
        .map(service -> service.getSubscriptionStatus(account))
        .orElse(AnimalInfoNotifStatus.NONE);

    var animalInfoStatus = subscriberRegistryServiceStrategy
        .getServiceByTopic(AnimalShelterTopic.ANIMAL_INFO.getTopicName())
        .map(service -> service.getSubscriptionStatus(account))
        .orElse(AnimalInfoNotifStatus.NONE);

    var vaccinationStatus = subscriberRegistryServiceStrategy
        .getServiceByTopic(AnimalShelterTopic.VACCINATION_INFO.getTopicName())
        .map(service -> service.getSubscriptionStatus(account))
        .orElse(AnimalInfoNotifStatus.NONE);

    return new NotificationStatusDTO(animalNewsStatus, animalInfoStatus, vaccinationStatus);
  }
}
