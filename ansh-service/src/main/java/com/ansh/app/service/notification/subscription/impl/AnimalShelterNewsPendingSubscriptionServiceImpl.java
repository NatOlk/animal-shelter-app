package com.ansh.app.service.notification.subscription.impl;

import com.ansh.app.service.notification.subscription.PendingSubscriptionService;
import com.ansh.app.service.user.UserProfileService;
import com.ansh.app.service.user.UserSubscriptionAuthorityService;
import com.ansh.event.AnimalShelterTopic;
import com.ansh.notification.subscription.PendingSubscriptionDecisionProducer;
import com.ansh.repository.PendingSubscriberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("animalShelterNewsPendingSubscriptionService")
public class AnimalShelterNewsPendingSubscriptionServiceImpl extends
    AbstractPendingSubscriptionService
    implements PendingSubscriptionService {

  @Autowired
  public AnimalShelterNewsPendingSubscriptionServiceImpl(
      PendingSubscriberRepository pendingSubscriberRepository,
      UserSubscriptionAuthorityService userSubscriptionAuthorityService,
      PendingSubscriptionDecisionProducer pendingSubscriptionDecisionProducer,
      UserProfileService userProfileService) {
    super(AnimalShelterTopic.ANIMAL_SHELTER_NEWS.getTopicName(), pendingSubscriberRepository,
        userSubscriptionAuthorityService,
        pendingSubscriptionDecisionProducer, userProfileService);
  }
}
