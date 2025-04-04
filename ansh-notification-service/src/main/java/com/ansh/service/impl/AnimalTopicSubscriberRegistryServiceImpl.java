package com.ansh.service.impl;

import static com.ansh.entity.account.UserProfile.AnimalInfoNotifStatus.ACTIVE;
import static com.ansh.entity.account.UserProfile.AnimalInfoNotifStatus.NONE;
import static com.ansh.entity.account.UserProfile.AnimalInfoNotifStatus.PENDING;

import com.ansh.cache.SubscriptionCacheManager;
import com.ansh.entity.account.UserProfile.AnimalInfoNotifStatus;
import com.ansh.entity.subscription.Subscription;
import com.ansh.notification.subscription.SubscriberNotificationEventProducer;
import com.ansh.repository.SubscriptionRepository;
import com.ansh.service.AnimalTopicSubscriberRegistryService;
import com.ansh.service.SubscriptionNotificationEmailService;
import com.ansh.utils.IdentifierMasker;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class AnimalTopicSubscriberRegistryServiceImpl implements
    AnimalTopicSubscriberRegistryService {

  private static final Logger LOG = LoggerFactory.getLogger(
      AnimalTopicSubscriberRegistryServiceImpl.class);

  @Value("${animalTopicId}")
  private String animalTopicId;

  @Autowired
  private SubscriptionRepository subscriptionRepository;

  @Autowired
  private SubscriptionNotificationEmailService subscriptionNotificationEmailService;

  @Autowired
  private SubscriberNotificationEventProducer subscriberNotificationEventProducer;

  @Autowired
  private SubscriptionCacheManager cacheManager;

  @PostConstruct
  protected void initializeCache() {
    cacheManager.initializeCache();
  }

  @Scheduled(cron = "0 0 20 * * ?", zone = "Europe/Berlin")
  protected void reloadCache() {
    if (cacheManager.shouldUpdateCache()) {
      cacheManager.reloadCache();
    }
  }

  @Override
  @Transactional
  public void registerSubscriber(@NonNull String email, String approver) {
    findSubscriptionByEmail(email).ifPresentOrElse(
        this::handleExistingSubscription,
        () -> createAndRegisterNewSubscription(email, approver)
    );
  }


  @Transactional
  public void unregisterSubscriber(@NonNull String token) {
    removeSubscriptionFromCacheAndDb(token);
  }

  @Override
  @Transactional
  public boolean acceptSubscription(@NonNull String token) {
    Optional<Subscription> subscriptionOpt = findSubscriptionByToken(token);
    subscriptionOpt.ifPresent(subscription -> {
      subscription.setAccepted(true);
      subscriptionRepository.save(subscription);
      cacheManager.addToCache(subscription);
      subscriptionNotificationEmailService.sendSuccessTokenConfirmationEmail(subscription);
    });
    return subscriptionOpt.isPresent();
  }

  @Override
  public List<Subscription> getAcceptedAndApprovedSubscribers() {
    return cacheManager.getAllFromCache().stream()
        .toList();
  }

  @Override
  public List<Subscription> getAllSubscriptions(@NonNull String approver) {
    return subscriptionRepository.findByApproverAndTopic(approver, animalTopicId);
  }

  @Override
  public AnimalInfoNotifStatus getSubscriptionStatus(@NonNull String approver) {
    return findSubscriptionByEmail(approver)
        .map(subscription -> subscription.isAccepted() ? ACTIVE : PENDING)
        .orElse(NONE);
  }

  @Override
  @Transactional
  public void handleSubscriptionApproval(@NonNull String email, String approver, boolean reject) {
    findSubscriptionByEmail(email).ifPresent(subscription -> {
      if (reject) {
        removeSubscriptionFromCacheAndDb(subscription.getToken());
      } else {
        approveSubscription(subscription, approver);
      }
    });
  }

  private void approveSubscription(Subscription subscription, String approver) {
    subscription.setApprover(approver);
    subscription.setApproved(true);
    subscriptionRepository.save(subscription);
    subscriptionNotificationEmailService.sendNeedAcceptSubscriptionEmail(subscription);
  }

  private void removeSubscriptionFromCacheAndDb(String token) {
    subscriptionRepository.deleteByTokenAndTopic(token, animalTopicId);
    cacheManager.removeFromCache(token);
  }

  private void createAndRegisterNewSubscription(String email, String approver) {
    if(approver == null) approver = "";
    Subscription newSubscription = Subscription.builder()
        .topic(animalTopicId)
        .email(email)
        .approver(approver)
        .token(UUID.randomUUID().toString())
        .accepted(false)
        .approved(false)
        .build();
    subscriptionRepository.save(newSubscription);
    subscriberNotificationEventProducer.sendPendingApproveRequest(email, approver, animalTopicId);
    LOG.debug("Register a new subscriber {}", IdentifierMasker.maskEmail(email));
  }

  private void handleExistingSubscription(Subscription subscription) {
    if (!subscription.isApproved()) {
      return;
    }
    if (subscription.isAccepted()) {
      subscriptionNotificationEmailService.sendRepeatConfirmationEmail(subscription);
    } else {
      subscriptionNotificationEmailService.sendNeedAcceptSubscriptionEmail(subscription);
    }
  }

  private Optional<Subscription> findSubscriptionByEmail(String email) {
    return subscriptionRepository.findByEmailAndTopic(email, animalTopicId);
  }

  private Optional<Subscription> findSubscriptionByToken(String token) {
    return subscriptionRepository.findByTokenAndTopic(token, animalTopicId);
  }

  protected void setAnimalTopicId(String animalTopicId) {
    this.animalTopicId = animalTopicId;
  }
}
