package com.ansh.app.service.notification.subscription.impl;

import com.ansh.app.service.notification.subscription.PendingSubscriptionService;
import com.ansh.app.service.user.UserSubscriptionAuthorityService;
import com.ansh.app.service.user.impl.UserProfileServiceImpl;
import com.ansh.notification.subscription.PendingSubscriptionDecisionProducer;
import com.ansh.repository.PendingSubscriberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("animalShelterNewsPendingSubscriptionService")
public class AnimalShelterNewsPendingSubscriptionServiceImpl extends AbstractPendingSubscriptionService
    implements PendingSubscriptionService {

  @Autowired
  public AnimalShelterNewsPendingSubscriptionServiceImpl(
      @Value("${animalShelterNewsTopicId}") String animalShelterNewsTopicId,
      PendingSubscriberRepository pendingSubscriberRepository,
      UserSubscriptionAuthorityService userSubscriptionAuthorityService,
      PendingSubscriptionDecisionProducer pendingSubscriptionDecisionProducer,
      UserProfileServiceImpl userProfileService) {
    super(animalShelterNewsTopicId, pendingSubscriberRepository, userSubscriptionAuthorityService,
        pendingSubscriptionDecisionProducer, userProfileService);
  }
}
