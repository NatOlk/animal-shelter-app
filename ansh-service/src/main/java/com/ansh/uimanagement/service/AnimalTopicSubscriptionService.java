package com.ansh.uimanagement.service;

import com.ansh.auth.service.UserProfileService;
import com.ansh.notification.AnimalNotificationUserSubscribedProducer;
import com.ansh.repository.PendingSubscriberRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AnimalTopicSubscriptionService {

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
          subscriber.setApprover(approver);
          animalNotificationUserSubscribedProducer.sendApprove(subscriber.getEmail(),
              subscriber.getApprover(), subscriber.getTopic());
          pendingSubscriberRepository.deleteByEmailAndTopic(email, animalTopicId);
        });
  }

  @Transactional
  public void rejectSubscriber(String email) {
    pendingSubscriberRepository.findByEmailAndTopic(email, animalTopicId)
        .ifPresent(subscriber -> {
          pendingSubscriberRepository.deleteByEmailAndTopic(email, animalTopicId);
          animalNotificationUserSubscribedProducer.sendReject(subscriber.getEmail(),
              subscriber.getApprover(), subscriber.getTopic());
        });
  }

  protected void setAnimalTopicId(String animalTopicId) {
    this.animalTopicId = animalTopicId;
  }
}
