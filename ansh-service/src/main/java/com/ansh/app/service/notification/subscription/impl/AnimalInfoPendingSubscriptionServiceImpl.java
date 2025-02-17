package com.ansh.app.service.notification.subscription.impl;

import com.ansh.app.service.user.UserSubscriptionAuthorityService;
import com.ansh.app.service.user.impl.UserProfileServiceImpl;
import com.ansh.notification.subscription.PendingSubscriptionDecisionProducer;
import com.ansh.repository.PendingSubscriberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("animalInfoPendingSubscriptionService")
public class AnimalInfoPendingSubscriptionServiceImpl extends AbstractPendingSubscriptionService {

  @Autowired
  public AnimalInfoPendingSubscriptionServiceImpl(
      @Value("${animalTopicId}") String animalTopicId,
      PendingSubscriberRepository pendingSubscriberRepository,
      UserSubscriptionAuthorityService userSubscriptionAuthorityService,
      PendingSubscriptionDecisionProducer pendingSubscriptionDecisionProducer,
      UserProfileServiceImpl userProfileService) {
    super(animalTopicId, pendingSubscriberRepository, userSubscriptionAuthorityService,
        pendingSubscriptionDecisionProducer, userProfileService);
  }
}
