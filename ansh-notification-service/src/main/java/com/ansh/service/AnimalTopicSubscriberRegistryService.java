package com.ansh.service;

import com.ansh.entity.subscription.Subscription;
import com.ansh.notification.SubscriberNotificationInfoProducer;
import com.ansh.repository.SubscriptionRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AnimalTopicSubscriberRegistryService {

  @Value("${animalTopicId}")
  private String animalTopicId;

  @Autowired
  private SubscriptionRepository subscriptionRepository;

  @Autowired
  private SubscriptionNotificationService subscriptionNotificationService;

  @Autowired
  private SubscriberNotificationInfoProducer subscriberNotificationInfoProducer;

  @Autowired
  private EmailService emailService;

  @Transactional
  public void registerSubscriber(String email, String approver) {
    List<Subscription> subscriptions = subscriptionRepository.findByEmailAndTopic(email,
        animalTopicId);
    if (subscriptions.isEmpty()) {
      Subscription sb = new Subscription();
      sb.setTopic(animalTopicId);
      sb.setEmail(email);
      sb.setAccepted(false);
      sb.setApprover(approver);
      sb.setApproved(false);
      sb.setToken(generateConfirmationToken());
      subscriptionRepository.save(sb);

      subscriberNotificationInfoProducer.sendApproveRequest(email, approver, animalTopicId);
    } else {

      Subscription sb = subscriptions.get(0);
      if (sb.isApproved()) {
        if (sb.isAccepted()) {
          subscriptionNotificationService.sendRepeatConfirmationEmail(sb);
        } else {
          subscriptionNotificationService.sendNeedAcceptSubscriptionEmail(sb);
        }
      }
    }
  }

  @Transactional
  public void unregisterSubscriber(String token) {
    subscriptionRepository.deleteByTokenAndTopic(token, animalTopicId);
  }

  @Transactional
  public void approveSubscriber(String email, String approver, String topic) {
    List<Subscription> subscriptions = subscriptionRepository.findByEmailAndTopic(email, topic);
    if (!subscriptions.isEmpty()) {
      Subscription sb = subscriptions.get(0);
      sb.setApprover(approver);
      sb.setApproved(true);
      subscriptionRepository.save(sb);
      subscriptionNotificationService.sendNeedAcceptSubscriptionEmail(sb);
    }
  }

  @Transactional
  public boolean confirmSubscription(String token) {
    AtomicBoolean updated = new AtomicBoolean(false);
    subscriptionRepository.findByToken(token)
        .forEach(subscription -> {
          subscription.setAccepted(true);
          subscriptionRepository.save(subscription);
          subscriptionNotificationService.sendSuccessTokenConfirmationEmail(subscription);
          updated.set(true);
        });
    return updated.get();
  }

  public List<Subscription> getAcceptedAndApprovedSubscribers() {
    return subscriptionRepository.findByTopicAndAcceptedTrueAndApprovedTrue(animalTopicId);
  }

  public List<Subscription> getAllSubscriptions(String approver) {
    return subscriptionRepository.findByTopicAndApprover(animalTopicId, approver);
  }

  private String generateConfirmationToken() {
    return UUID.randomUUID().toString();
  }
}
