package com.ansh.service.impl;

import com.ansh.entity.subscription.Subscription;
import com.ansh.event.AnimalShelterTopic;
import com.ansh.service.SubscriberRegistryService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("animalTopicSubscriber")
public class AnimalTopicSubscriberRegistryServiceImpl extends AbstractSubscriberRegistryService
    implements SubscriberRegistryService {

  @Override
  public String getTopicId() {
    return AnimalShelterTopic.ANIMAL_INFO.getTopicName();
  }


  @Override
  protected void approveSubscription(Subscription subscription, String approver) {
    super.acceptSubscription(subscription.getToken());
    super.approveSubscription(subscription, approver);
  }
}
