package com.ansh.app.service.notification.subscription.impl;

import com.ansh.app.service.notification.subscription.PendingSubscriptionService;
import com.ansh.app.service.user.UserProfileService;
import com.ansh.event.AnimalShelterTopic;
import com.ansh.notification.subscription.PendingSubscriptionDecisionProducer;
import com.ansh.repository.PendingSubscriberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("vaccinationInfoPendingSubscriptionService")
public class VaccinationInfoPendingSubscriptionServiceImpl extends
    AbstractPendingSubscriptionService
    implements PendingSubscriptionService {

  @Autowired
  public VaccinationInfoPendingSubscriptionServiceImpl(
      PendingSubscriberRepository pendingSubscriberRepository,
      PendingSubscriptionDecisionProducer pendingSubscriptionDecisionProducer,
      UserProfileService userProfileService) {
    super(AnimalShelterTopic.VACCINATION_INFO.getTopicName(), pendingSubscriberRepository,
        pendingSubscriptionDecisionProducer, userProfileService);
  }
}
