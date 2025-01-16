package com.ansh.uimanagement.service;

import com.ansh.auth.service.UserProfileService;
import com.ansh.notification.AnimalNotificationUserSubscribedProducer;
import com.ansh.repository.PendingSubscriberRepository;
import com.ansh.utils.IdentifierMasker;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AnimalTopicSubscriptionService {

  private static final Logger LOG = LoggerFactory.getLogger(AnimalTopicSubscriptionService.class);

  @Value("${animalTopicId}")
  private String animalTopicId;

  @Autowired
  private AnimalNotificationUserSubscribedProducer animalNotificationUserSubscribedProducer;

  @Autowired
  private PendingSubscriberRepository pendingSubscriberRepository;
  @Autowired
  private UserProfileService userProfileService;


  @Transactional
  public void approveSubscriber(String email, String approver) {
    pendingSubscriberRepository.findByEmailAndTopic(email, animalTopicId)
        .ifPresent(subscriber -> {
          pendingSubscriberRepository.deleteByEmailAndTopic(email, animalTopicId);
          animalNotificationUserSubscribedProducer.sendApprove(subscriber.getEmail(),
              approver, subscriber.getTopic());
          LOG.debug("[animal topic subscription] approval is sent for {} , approver is {}.",
              IdentifierMasker.maskEmail(email), IdentifierMasker.maskEmail(approver));
        });
  }

  @Transactional
  public void rejectSubscriber(String email) {
    pendingSubscriberRepository.findByEmailAndTopic(email, animalTopicId)
        .ifPresent(subscriber -> {
          pendingSubscriberRepository.deleteByEmailAndTopic(email, animalTopicId);
          animalNotificationUserSubscribedProducer.sendReject(subscriber.getEmail(),
              subscriber.getApprover(), subscriber.getTopic());
          LOG.debug("[animal topic subscription] rejection is sent for {} ",
              IdentifierMasker.maskEmail(email));
        });
  }

  protected void setAnimalTopicId(String animalTopicId) {
    this.animalTopicId = animalTopicId;
  }
}
