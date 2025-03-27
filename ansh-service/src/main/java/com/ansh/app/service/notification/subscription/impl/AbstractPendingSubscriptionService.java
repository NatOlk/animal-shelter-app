package com.ansh.app.service.notification.subscription.impl;

import static com.ansh.entity.account.UserProfile.AnimalInfoNotifStatus.PENDING;

import com.ansh.app.service.exception.user.UnauthorizedActionException;
import com.ansh.app.service.notification.subscription.PendingSubscriptionService;
import com.ansh.app.service.user.UserSubscriptionAuthorityService;
import com.ansh.app.service.user.impl.UserProfileServiceImpl;
import com.ansh.notification.subscription.PendingSubscriptionDecisionProducer;
import com.ansh.repository.PendingSubscriberRepository;
import com.ansh.repository.entity.PendingSubscriber;
import com.ansh.utils.IdentifierMasker;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractPendingSubscriptionService implements PendingSubscriptionService {

  private static final Logger LOG = LoggerFactory.getLogger(
      AbstractPendingSubscriptionService.class);
  protected final String topicId;
  protected final PendingSubscriberRepository pendingSubscriberRepository;
  protected final UserSubscriptionAuthorityService userSubscriptionAuthorityService;
  protected final PendingSubscriptionDecisionProducer pendingSubscriptionDecisionProducer;
  protected final UserProfileServiceImpl userProfileService;

  protected AbstractPendingSubscriptionService(
      String topicId,
      PendingSubscriberRepository pendingSubscriberRepository,
      UserSubscriptionAuthorityService userSubscriptionAuthorityService,
      PendingSubscriptionDecisionProducer pendingSubscriptionDecisionProducer,
      UserProfileServiceImpl userProfileService) {
    this.topicId = topicId;
    this.pendingSubscriberRepository = pendingSubscriberRepository;
    this.userSubscriptionAuthorityService = userSubscriptionAuthorityService;
    this.pendingSubscriptionDecisionProducer = pendingSubscriptionDecisionProducer;
    this.userProfileService = userProfileService;
  }

  @Override
  @Transactional
  public void approveSubscriber(String email, String approver) throws UnauthorizedActionException {
    userSubscriptionAuthorityService.checkAuthorityToApprove(approver);
    findPendingSubscriber(email)
        .ifPresent(subscriber -> {
          deletePendingSubscriber(email);
          pendingSubscriptionDecisionProducer.sendApprove(subscriber.getEmail(), approver,
              subscriber.getTopic());
          LOG.debug("[{} subscription] approval sent for {} by {}", topicId,
              IdentifierMasker.maskEmail(email), IdentifierMasker.maskEmail(approver));
        });
  }

  @Override
  @Transactional
  public void rejectSubscriber(String email, String approver) throws UnauthorizedActionException {
    userSubscriptionAuthorityService.checkAuthorityToReject(approver);
    findPendingSubscriber(email)
        .ifPresent(subscriber -> {
          deletePendingSubscriber(email);
          pendingSubscriptionDecisionProducer.sendReject(subscriber.getEmail(),
              subscriber.getApprover(), subscriber.getTopic());
          LOG.debug("[{} subscription] rejection sent for {}", topicId,
              IdentifierMasker.maskEmail(email));
        });
  }

  @Override
  @Transactional
  public void saveSubscriber(String email, String approver) {
    if (findPendingSubscriber(email).isEmpty()) {
      PendingSubscriber newSubscriber = new PendingSubscriber(email, approver, topicId);
      pendingSubscriberRepository.save(newSubscriber);
      if (LOG.isDebugEnabled()) {
        LOG.debug("[save pending subscriber] {} for topic {}",
            IdentifierMasker.maskEmail(email), topicId);
      }
      userProfileService.updateAnimalNotificationSubscriptionStatus(email, PENDING);
    }
  }

  @Override
  public List<PendingSubscriber> getSubscribersByApprover(String approver) {
    return pendingSubscriberRepository.findByApproverAndTopic(approver, topicId);
  }

  @Override
  public List<PendingSubscriber> getPendingSubscribersWithoutApprover() {
    return pendingSubscriberRepository.findByTopicAndApproverIsNullOrEmpty(topicId);
  }

  @Override
  public String getTopicId() {
    return topicId;
  }

  private Optional<PendingSubscriber> findPendingSubscriber(String email) {
    return pendingSubscriberRepository.findByEmailAndTopic(email, topicId);
  }

  private void deletePendingSubscriber(String email) {
    pendingSubscriberRepository.deleteByEmailAndTopic(email, topicId);
  }
}
