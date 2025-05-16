package com.ansh.service.impl;

import static com.ansh.entity.account.UserProfile.AnimalInfoNotifStatus.ACTIVE;
import static com.ansh.entity.account.UserProfile.AnimalInfoNotifStatus.NONE;
import static com.ansh.entity.account.UserProfile.AnimalInfoNotifStatus.PENDING;

import com.ansh.cache.SubscriptionCacheManager;
import com.ansh.entity.account.UserProfile.AnimalInfoNotifStatus;
import com.ansh.entity.subscription.Subscription;
import com.ansh.notification.subscription.SubscriberNotificationEventProducer;
import com.ansh.repository.SubscriptionRepository;
import com.ansh.service.SubscriberRegistryService;
import com.ansh.utils.IdentifierMasker;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;

public abstract class AbstractSubscriberRegistryService implements SubscriberRegistryService {

  protected static final Logger LOG = LoggerFactory.getLogger(
      AbstractSubscriberRegistryService.class);

  @Autowired
  protected SubscriptionRepository subscriptionRepository;

  @Autowired
  protected SubscriberNotificationEventProducer subscriberNotificationEventProducer;

  @Autowired
  protected SubscriptionCacheManager cacheManager;

  /**
   * Handles an already existing subscription.
   */
  protected void handleExistingSubscription(Subscription subscription) {
    LOG.debug("Existing subscription found for email: {}, topic: {}. Skipping creation.",
        IdentifierMasker.maskEmail(subscription.getEmail()), getTopicId());
  }

  /**
   * Sends a confirmation email after a subscription token is successfully accepted.
   */
  protected void sendSuccessTokenConfirmationEmail(Subscription subscription) {
  }

  /**
   * Sends an email prompting the user to accept their subscription (e.g., via token link).
   */
  protected void sendNeedAcceptSubscriptionEmail(Subscription subscription) {
  }

  /**
   * Determines whether the subscription should be automatically accepted upon approval.
   * <p>
   * This method can be overridden in subclasses to specify if subscriptions for a given topic
   * should bypass manual confirmation and be activated immediately after approval.
   * </p>
   * <p>
   * Returns {@code false} by default, meaning manual confirmation is still required.
   * </p>
   *
   * @return {@code true} if the subscription should be auto-accepted, {@code false} otherwise
   */
  protected boolean isAutoAccept() {
    return false;
  }

  @Override
  @Transactional
  public void registerSubscriber(@NonNull String email, String approver) {
    findSubscriptionByEmail(email).ifPresentOrElse(
        this::handleExistingSubscription,
        () -> createAndRegisterNewSubscription(email, approver)
    );
  }

  @Override
  @Transactional
  public void unsubscribe(@NonNull String token) {
    removeSubscriptionFromCacheAndDb(token);
  }

  @Override
  @Transactional
  public void unsubscribe(@NonNull String email, String approver) {
    removeSubscriptionFromCacheAndDb(email, approver);
  }

  @Override
  public List<Subscription> getAcceptedAndApprovedSubscribers() {
    return cacheManager.getAllFromCache(getTopicId());
  }

  @Override
  public List<Subscription> getAllSubscriptions(@NonNull String approver) {
    return subscriptionRepository.findByApproverAndTopic(approver, getTopicId());
  }

  @Override
  public AnimalInfoNotifStatus getSubscriptionStatus(@NonNull String email) {
    return findSubscriptionByEmail(email)
        .map(subscription -> subscription.isAccepted() ? ACTIVE : PENDING)
        .orElse(NONE);
  }

  @Override
  @Transactional
  public boolean acceptSubscription(@NonNull String token) {
    Optional<Subscription> subscriptionOpt = findSubscriptionByToken(token);
    subscriptionOpt.ifPresent(this::accept);
    return subscriptionOpt.isPresent();
  }

  protected void accept(Subscription subscription) {
    LOG.debug("Accepting subscription for {} in topic {}",
        IdentifierMasker.maskEmail(subscription.getEmail()), getTopicId());
    subscription.setAccepted(true);
    subscriptionRepository.save(subscription);
    cacheManager.addToCache(subscription);
    sendSuccessTokenConfirmationEmail(subscription);
  }

  @Override
  @Transactional
  public void handleSubscriptionApproval(@NonNull String email, String approver, boolean reject) {
    findSubscriptionByEmail(email).ifPresentOrElse(subscription -> {
      if (reject) {
        removeSubscriptionFromCacheAndDb(subscription.getToken());
      } else {
        approveSubscription(subscription, approver);
      }
    }, () -> LOG.warn("No subscription found for email: {}", email));
  }

  protected void approveSubscription(Subscription subscription, String approver) {
    LOG.debug("Approving subscription for {} by approver {} in topic {}",
        IdentifierMasker.maskEmail(subscription.getEmail()), approver, getTopicId());
    subscription.setApprover(approver);
    subscription.setApproved(true);
    subscriptionRepository.save(subscription);
    sendNeedAcceptSubscriptionEmail(subscription);
    if (isAutoAccept()) {
      accept(subscription);
    }
  }

  private void removeSubscriptionFromCacheAndDb(String token) {
    subscriptionRepository.deleteByTokenAndTopic(token, getTopicId());
    cacheManager.removeFromCache(token);
  }

  private void removeSubscriptionFromCacheAndDb(String email, String approver) {
    subscriptionRepository.findByEmailAndTopic(email, getTopicId())
        .ifPresent(subscription -> {
              subscriptionRepository.deleteByEmailAndTopic(email, getTopicId());
              cacheManager.removeFromCache(subscription.getToken());
            }
        );
  }

  private void createAndRegisterNewSubscription(String email, String approver) {
    if (approver == null) {
      approver = "";
    }
    Subscription newSubscription = Subscription.builder()
        .topic(getTopicId())
        .email(email)
        .approver(approver)
        .token(UUID.randomUUID().toString())
        .accepted(false)
        .approved(false)
        .build();
    subscriptionRepository.save(newSubscription);
    subscriberNotificationEventProducer.sendPendingApproveRequest(email, approver, getTopicId());
    LOG.debug("Register a new subscriber {} for topic {}", IdentifierMasker.maskEmail(email),
        getTopicId());
  }

  private Optional<Subscription> findSubscriptionByEmail(String email) {
    return subscriptionRepository.findByEmailAndTopic(email, getTopicId());
  }

  private Optional<Subscription> findSubscriptionByToken(String token) {
    return subscriptionRepository.findByTokenAndTopic(token, getTopicId());
  }
}
