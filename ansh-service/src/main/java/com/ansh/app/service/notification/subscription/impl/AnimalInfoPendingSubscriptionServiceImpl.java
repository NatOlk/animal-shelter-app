package com.ansh.app.service.notification.subscription.impl;

import static com.ansh.entity.animal.UserProfile.AnimalNotifStatus.PENDING;

import com.ansh.app.service.exception.user.UnauthorizedActionException;
import com.ansh.app.service.notification.subscription.AnimalInfoPendingSubscriptionService;
import com.ansh.app.service.user.UserAuthorityService;
import com.ansh.app.service.user.impl.UserProfileServiceImpl;
import com.ansh.notification.subscription.AnimalNotificationUserSubscribedProducer;
import com.ansh.repository.PendingSubscriberRepository;
import com.ansh.repository.entity.PendingSubscriber;
import com.ansh.utils.IdentifierMasker;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("animalInfoPendingSubscriptionService")
public class AnimalInfoPendingSubscriptionServiceImpl implements
    AnimalInfoPendingSubscriptionService {

  private static final Logger LOG = LoggerFactory.getLogger(
      AnimalInfoPendingSubscriptionServiceImpl.class);

  @Value("${animalTopicId}")
  private String animalTopicId;

  @Autowired
  @Qualifier("animalNotificationUserSubscribedProducer")
  private AnimalNotificationUserSubscribedProducer subscriptionProducer;

  @Autowired
  private PendingSubscriberRepository pendingSubscriberRepository;

  @Autowired
  private UserProfileServiceImpl userProfileService;

  @Autowired
  private UserAuthorityService userAuthorityService;

  @Override
  @Transactional
  public void approveSubscriber(String email, String approver) throws UnauthorizedActionException {
    userAuthorityService.checkAuthorityToApprove(approver);
    findPendingSubscriber(email)
        .ifPresent(subscriber -> {
          deletePendingSubscriber(email);
          subscriptionProducer.sendApprove(subscriber.getEmail(),
              approver, subscriber.getTopic());
          LOG.debug("[animal topic subscription] approval is sent for {} , approver is {}.",
              IdentifierMasker.maskEmail(email), IdentifierMasker.maskEmail(approver));
        });
  }

  @Override
  @Transactional
  public void rejectSubscriber(String email, String approver) throws UnauthorizedActionException {
    userAuthorityService.checkAuthorityToReject(approver);
    findPendingSubscriber(email)
        .ifPresent(subscriber -> {
          deletePendingSubscriber(email);
          subscriptionProducer.sendReject(subscriber.getEmail(),
              subscriber.getApprover(), subscriber.getTopic());
          LOG.debug("[animal topic subscription] rejection is sent for {} ",
              IdentifierMasker.maskEmail(email));
        });
  }

  @Override
  @Transactional
  public void saveSubscriber(String email, String approver) {
    if (findPendingSubscriber(email).isEmpty()) {
      PendingSubscriber newSubscriber = new PendingSubscriber(email, approver, animalTopicId);
      pendingSubscriberRepository.save(newSubscriber);
      LOG.debug("[pending subscriber] {} ",
          IdentifierMasker.maskEmail(newSubscriber.getEmail()));
      userProfileService.updateAnimalNotificationSubscriptionStatus(email, PENDING);
    }
  }

  @Override
  public List<PendingSubscriber> getSubscribersByApprover(String approver) {
    return pendingSubscriberRepository.findByApproverAndTopic(approver, animalTopicId);
  }

  @Override
  public List<PendingSubscriber> getPendingSubscribersWithoutApprover() {
    return pendingSubscriberRepository.findByTopicAndApproverIsNullOrEmpty(animalTopicId);
  }

  private Optional<PendingSubscriber> findPendingSubscriber(String email) {
    return pendingSubscriberRepository.findByEmailAndTopic(email, animalTopicId);
  }

  private void deletePendingSubscriber(String email) {
    pendingSubscriberRepository.deleteByEmailAndTopic(email, animalTopicId);
  }

  protected void setAnimalTopicId(String animalTopicId) {
    this.animalTopicId = animalTopicId;
  }
}
