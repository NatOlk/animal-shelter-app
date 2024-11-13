package com.ansh.service;

import static com.ansh.entity.animal.UserProfile.AnimalNotificationSubscriptionStatus.ACTIVE;
import static com.ansh.entity.animal.UserProfile.AnimalNotificationSubscriptionStatus.NONE;
import static com.ansh.entity.animal.UserProfile.AnimalNotificationSubscriptionStatus.PENDING;

import com.ansh.entity.animal.UserProfile;
import com.ansh.entity.subscription.Subscription;
import com.ansh.notification.SubscriberNotificationInfoProducer;
import com.ansh.repository.SubscriptionRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class AnimalTopicSubscriberRegistryService {

  private static final Logger LOG = LoggerFactory.getLogger(
      AnimalTopicSubscriberRegistryService.class);

  private static final String SUBSCRIPTIONS_CACHE = "animalNotificationSubscriptionsCache";

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

  @Autowired
  private RedisTemplate<String, Subscription> redisTemplate;

  @PostConstruct
  public void initializeCache() {
    List<Subscription> subscriptions = subscriptionRepository.findByTopicAndAcceptedTrueAndApprovedTrue(animalTopicId);
    LOG.debug("[init] Starting to fulfill cache with subscription qtx: {}", subscriptions.size());
    subscriptions.forEach(subscription -> {
      redisTemplate.opsForValue()
          .set(SUBSCRIPTIONS_CACHE + ":" + subscription.getToken(), subscription);
      LOG.debug("[init] Have put subscription with key {}", subscription.getToken());
    });
  }

  @Scheduled(cron = "0 0 20 * * ?", zone = "Europe/Berlin")
  public void reloadCache() {
    LOG.info("[reload] Current time: {} - Perfect timing for a cache reload!", LocalTime.now());
    clearCache();
    initializeCache();
    LOG.debug("[reload] Cache has been reloaded with updated subscriptions");
  }

  @Transactional
  public void registerSubscriber(String email, String approver) {
    findSubscriptionByEmail(email)
        .ifPresentOrElse(this::resendExistingSubscriptionNotification,
            () -> createAndRegisterNewSubscription(email, approver));
  }

  @Transactional
  public void unregisterSubscriber(String token) {
    removeSubscriptionFromCacheAndDb(token);
  }

  @Transactional
  public void handleSubscriptionApproval(String email, String approver, boolean reject) {
    findSubscriptionByEmail(email).ifPresent(subscription -> {
      if (reject) {
        removeSubscriptionFromCacheAndDb(subscription.getToken());
      } else {
        approveSubscription(subscription, approver);
      }
    });
  }

  @Transactional
  public boolean acceptSubscription(String token) {
    List<Subscription> subscriptions = subscriptionRepository.findByToken(token);

    subscriptions.forEach(subscription -> {
      subscription.setAccepted(true);
      subscriptionRepository.save(subscription);
      cacheSubscription(subscription);
      subscriptionNotificationService.sendSuccessTokenConfirmationEmail(subscription);
    });

    return !subscriptions.isEmpty();
  }

  private void clearCache() {
    Set<String> keys = redisTemplate.keys(SUBSCRIPTIONS_CACHE + ":*");
    if (keys != null) {
      RedisTemplate<String, Subscription> subscriptionRedisTemplate = redisTemplate;
      for (String key : keys) {
        subscriptionRedisTemplate.delete(key);
      }
    }
  }

  private void createAndRegisterNewSubscription(String email, String approver) {
    Subscription newSubscription = new Subscription();
    newSubscription.setTopic(animalTopicId);
    newSubscription.setEmail(email);
    newSubscription.setApprover(approver);
    newSubscription.setToken(UUID.randomUUID().toString());
    newSubscription.setAccepted(false);
    newSubscription.setApproved(false);

    subscriptionRepository.save(newSubscription);
    subscriberNotificationInfoProducer.sendApproveRequest(email, approver, animalTopicId);
  }


  private void cacheSubscription(Subscription subscription) {
    if (!subscription.isApproved() || !subscription.isAccepted()) {
      return;
    }
    redisTemplate.opsForValue()
        .set(SUBSCRIPTIONS_CACHE + ":" + subscription.getToken(), subscription);
    LOG.debug("[add] Have added approved and accepted subscription with key {}", subscription.getToken());
  }


  private void removeSubscriptionFromCacheAndDb(String token) {
    subscriptionRepository.deleteByTokenAndTopic(token, animalTopicId);
    redisTemplate.delete(SUBSCRIPTIONS_CACHE + ":" + token);
    LOG.debug("[remove] Have removed subscription with key {}", token);
  }

  public List<Subscription> getAcceptedAndApprovedSubscribersFromCache() {
    return getAllSubscriptionsFromCache().stream()
        .filter(Subscription::isAccepted)
        .filter(Subscription::isApproved)
        .toList();
  }

  public List<Subscription> getAllSubscriptions(String approver) {
    return subscriptionRepository.findByApproverAndTopic(approver, animalTopicId);
  }

  public UserProfile.AnimalNotificationSubscriptionStatus getStatusByApprover(String approver) {
    return findSubscriptionByEmail(approver)
        .map(subscription -> subscription.isAccepted() ? ACTIVE : PENDING)
        .orElse(NONE);
  }

  private void resendExistingSubscriptionNotification(Subscription sb) {
    if (!sb.isApproved()) {
      return;
    }
    if (sb.isAccepted()) {
      subscriptionNotificationService.sendRepeatConfirmationEmail(sb);
    } else {
      subscriptionNotificationService.sendNeedAcceptSubscriptionEmail(sb);
    }

  }

  private void approveSubscription(Subscription subscription, String approver) {
    subscription.setApprover(approver);
    subscription.setApproved(true);
    subscriptionRepository.save(subscription);
    subscriptionNotificationService.sendNeedAcceptSubscriptionEmail(subscription);
  }

  private Optional<Subscription> findSubscriptionByEmail(String email) {
    return subscriptionRepository.findByEmailAndTopic(email, animalTopicId).stream().findFirst();
  }

  private List<Subscription> getAllSubscriptionsFromCache() {
    Set<String> keys = redisTemplate.keys(SUBSCRIPTIONS_CACHE + ":*");
    LOG.debug("[all] Get all subscriptions: Keys: {}", keys);
    List<Subscription> subscriptions = new ArrayList<>();
    if (keys != null) {
      keys.stream()
          .map(key -> redisTemplate.opsForValue().get(key))
          .forEach(subscriptions::add);
    }
    return subscriptions;
  }
}
