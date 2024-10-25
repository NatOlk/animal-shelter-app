package com.ansh.service;


import com.ansh.repository.SubscriptionRepository;
import com.ansh.repository.entity.Subscription;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TopicSubscriberRegistryService {

  @Value("${animalGroupTopicId}")
  private String animalGroupTopicId;

  @Autowired
  private SubscriptionRepository subscriptionRepository;

  @Autowired
  private SubscriptionNotificationService subscriptionNotificationService;

  @Autowired
  private EmailService emailService;

  @Transactional
  public void registerSubscriber(String email) {
    List<Subscription> subscriptions = subscriptionRepository.getSubscriptionByEmail(email);
    if (subscriptions.isEmpty()) {
      Subscription sb = new Subscription();
      sb.setTopic(animalGroupTopicId);
      sb.setEmail(email);
      sb.setAccepted(false);
      sb.setToken(generateConfirmationToken());
      subscriptionRepository.save(sb);

      subscriptionNotificationService.sendNeedAcceptSubscriptionEmail(sb);
    } else {
      Subscription sb = subscriptions.get(0);
      if (sb.isAccepted()) {
        subscriptionNotificationService.sendRepeatConfirmationEmail(sb);
      } else {
        subscriptionNotificationService.sendNeedAcceptSubscriptionEmail(sb);
      }
    }
  }

  @Transactional
  public void unregisterSubscriber(String token) {
    subscriptionRepository.removeAllByTokenAndTopic(token, animalGroupTopicId);
  }

  public List<Subscription> getSubscribers() {
    return subscriptionRepository.getAllSubscriptionsByTopic(animalGroupTopicId)
        .stream()
        .filter(Subscription::isAccepted)
        .toList();
  }

  public List<Subscription> getAllSubscribers() {
    return subscriptionRepository.getAllSubscriptionsByTopic(animalGroupTopicId)
        .stream()
        .toList();
  }


  @Transactional
  public boolean confirmSubscription(String token) {
    AtomicBoolean updated = new AtomicBoolean(false);
    subscriptionRepository.getAllSubscriptionsByToken(token)
        .forEach(subscription -> {
          subscription.setAccepted(true);
          subscriptionRepository.save(subscription);
          subscriptionNotificationService.sendSuccessTokenConfirmationEmail(subscription);
          updated.set(true);
        });
    return updated.get();
  }

  private String generateConfirmationToken() {
    return UUID.randomUUID().toString();
  }
}
