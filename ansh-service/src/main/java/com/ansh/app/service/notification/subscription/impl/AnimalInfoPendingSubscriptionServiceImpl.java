package com.ansh.app.service.notification.subscription.impl;

import com.ansh.app.service.notification.subscription.PendingSubscriptionService;
import com.ansh.app.service.user.UserProfileService;
import com.ansh.event.AnimalShelterTopic;
import com.ansh.notification.subscription.PendingSubscriptionDecisionProducer;
import com.ansh.repository.PendingSubscriberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("animalInfoPendingSubscriptionService")
public class AnimalInfoPendingSubscriptionServiceImpl extends AbstractPendingSubscriptionService
    implements PendingSubscriptionService {

  @Autowired
  public AnimalInfoPendingSubscriptionServiceImpl(
      PendingSubscriberRepository pendingSubscriberRepository,
      PendingSubscriptionDecisionProducer pendingSubscriptionDecisionProducer,
      UserProfileService userProfileService) {
    super(AnimalShelterTopic.ANIMAL_INFO.getTopicName(), pendingSubscriberRepository,
        pendingSubscriptionDecisionProducer, userProfileService);
  }
}
