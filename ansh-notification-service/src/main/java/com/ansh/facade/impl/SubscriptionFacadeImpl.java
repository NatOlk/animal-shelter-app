package com.ansh.facade.impl;

import com.ansh.dto.SubscriptionRequest;
import com.ansh.entity.account.UserProfile.AnimalInfoNotifStatus;
import com.ansh.entity.subscription.Subscription;
import com.ansh.facade.SubscriptionFacade;
import com.ansh.service.SubscriberRegistryService;
import com.ansh.strategy.SubscriberRegistryServiceStrategy;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionFacadeImpl implements SubscriptionFacade {

  @Autowired
  private SubscriberRegistryServiceStrategy subscriberRegistryServiceStrategy;

  @Override
  public List<Subscription> getAllSubscriptionByApprover(String approver) {
    List<Subscription> allSubscriptions = new ArrayList<>();
    for (SubscriberRegistryService service : subscriberRegistryServiceStrategy.getAllServices()) {
      allSubscriptions.addAll(service.getAllSubscriptions(approver));
    }
    return allSubscriptions;
  }

  @Override
  public void registerEmployeeSubscription(SubscriptionRequest req) {
    subscriberRegistryServiceStrategy.getServiceByTopic(req.getTopic())
        .ifPresent(service -> service.registerSubscriber(req.getEmail(), req.getApprover()));
  }

  @Override
  public void unregisterEmployeeSubscription(String token) {
    subscriberRegistryServiceStrategy.getAllServices()
        .forEach(service -> service.unregisterSubscriber(token));
  }

  @Override
  public Optional<AnimalInfoNotifStatus> getSubscriptionStatus(SubscriptionRequest req) {
    return subscriberRegistryServiceStrategy.getServiceByTopic(req.getTopic())
        .map(service -> service.getSubscriptionStatus(req.getEmail()));
  }
}
