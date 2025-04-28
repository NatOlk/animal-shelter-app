package com.ansh.app.service.notification.subscription.impl;

import com.ansh.app.service.notification.subscription.PendingSubscriptionService;
import com.ansh.app.service.user.UserProfileService;
import com.ansh.app.service.user.UserSubscriptionAuthorityService;
import com.ansh.notification.subscription.PendingSubscriptionDecisionProducer;
import com.ansh.repository.PendingSubscriberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("vaccinationInfoPendingSubscriptionService")
public class VaccinationInfoPendingSubscriptionServiceImpl extends
    AbstractPendingSubscriptionService
    implements PendingSubscriptionService {

  @Autowired
  public VaccinationInfoPendingSubscriptionServiceImpl(
      @Value("${vaccinationTopicId}") String vaccinationTopicId,
      PendingSubscriberRepository pendingSubscriberRepository,
      UserSubscriptionAuthorityService userSubscriptionAuthorityService,
      PendingSubscriptionDecisionProducer pendingSubscriptionDecisionProducer,
      UserProfileService userProfileService) {
    super(vaccinationTopicId, pendingSubscriberRepository, userSubscriptionAuthorityService,
        pendingSubscriptionDecisionProducer, userProfileService);
  }
}
