package com.ansh.service;

import static com.ansh.entity.animal.UserProfile.AnimalNotificationSubscriptionStatus.ACTIVE;
import static com.ansh.entity.animal.UserProfile.AnimalNotificationSubscriptionStatus.NONE;
import static com.ansh.entity.animal.UserProfile.AnimalNotificationSubscriptionStatus.PENDING;

import com.ansh.entity.animal.UserProfile;
import com.ansh.entity.subscription.Subscription;
import com.ansh.notification.SubscriberNotificationInfoProducer;
import com.ansh.repository.SubscriptionRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
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
    Optional<Subscription> subscriptionOpt = findAnimalTopicSubscription(email);

    if (subscriptionOpt.isEmpty()) {
      createNewAnimalTopicSubscription(email, approver);
    } else {
      resendExistingSubscriptionNotification(subscriptionOpt.get());
    }
  }

  @Transactional
  public void unregisterSubscriber(String token) {
    subscriptionRepository.deleteByTokenAndTopic(token, animalTopicId);
  }

  @Transactional
  public void handleSubscriptionApproval(String email, String approver, String topic,
      boolean reject) {
    Optional<Subscription> subscriptionOpt = findSubscriptionForTopic(email, topic);

    if (subscriptionOpt.isPresent()) {
      Subscription sb = subscriptionOpt.get();
      if (!reject) {
        approveSubscription(sb, approver);
      } else {
        subscriptionRepository.deleteByEmailAndTopic(email, topic);
      }
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
    return subscriptionRepository.findByApproverAndTopic(approver, animalTopicId);
  }

  public UserProfile.AnimalNotificationSubscriptionStatus getStatusByApprover(String approver) {
    Optional<Subscription> subscriptionOpt = findAnimalTopicSubscription(approver);

    if (subscriptionOpt.isEmpty()) {
      return NONE;
    }

    Subscription subscription = subscriptionOpt.get();
    return subscription.isAccepted() ? ACTIVE : PENDING;
  }

  private void createNewAnimalTopicSubscription(String email, String approver) {
    Subscription sb = new Subscription();
    sb.setTopic(animalTopicId);
    sb.setEmail(email);
    sb.setAccepted(false);
    sb.setApprover(approver);
    sb.setApproved(false);
    sb.setToken(generateConfirmationToken());
    subscriptionRepository.save(sb);
    subscriberNotificationInfoProducer.sendApproveRequest(email, approver, animalTopicId);
  }

  private void resendExistingSubscriptionNotification(Subscription sb) {
    if (sb.isApproved()) {
      if (sb.isAccepted()) {
        subscriptionNotificationService.sendRepeatConfirmationEmail(sb);
      } else {
        subscriptionNotificationService.sendNeedAcceptSubscriptionEmail(sb);
      }
    }
  }

  private void approveSubscription(Subscription sb, String approver) {
    sb.setApprover(approver);
    sb.setApproved(true);
    subscriptionRepository.save(sb);
    subscriptionNotificationService.sendNeedAcceptSubscriptionEmail(sb);
  }

  private Optional<Subscription> findAnimalTopicSubscription(String email) {
    return subscriptionRepository.findByEmailAndTopic(email, animalTopicId).stream().findFirst();
  }

  private Optional<Subscription> findSubscriptionForTopic(String email, String topic) {
    return subscriptionRepository.findByEmailAndTopic(email, topic).stream().findFirst();
  }

  private String generateConfirmationToken() {
    return UUID.randomUUID().toString();
  }
}
