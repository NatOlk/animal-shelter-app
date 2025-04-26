package com.ansh.service;

import com.ansh.entity.account.UserProfile.AnimalInfoNotifStatus;
import com.ansh.entity.subscription.Subscription;
import java.util.List;
import org.springframework.lang.NonNull;

/**
 * Service interface for managing subscriptions.
 */
public interface SubscriberRegistryService {

  /**
   * Returns the ID of the topic that this service is responsible for handling.
   *
   * @return the topic ID associated with this subscription service
   */
  String getTopicId();

  /**
   * Registers a new subscriber for animal topic notifications.
   *
   * @param email the email of the subscriber
   * @param approver the approver who can approve or reject the subscription
   */
  void registerSubscriber(@NonNull String email, String approver);

  /**
   * Unregisters a subscriber using their subscription token.
   *
   * @param token the unique token identifying the subscription
   */
  void unregisterSubscriber(@NonNull String token);

  /**
   * Accepts a subscription request based on the provided token.
   *
   * @param token the unique token identifying the subscription request
   * @return true if the subscription was successfully accepted, false otherwise
   */
  boolean acceptSubscription(@NonNull String token);

  /**
   * Retrieves a list of all subscribers who have been approved and accepted.
   *
   * @return a list of accepted and approved subscriptions
   */
  List<Subscription> getAcceptedAndApprovedSubscribers();

  /**
   * Retrieves all subscriptions associated with a specific approver.
   *
   * @param approver the email or identifier of the approver
   * @return a list of all subscriptions for the specified approver
   */
  List<Subscription> getAllSubscriptions(@NonNull String approver);

  /**
   * Retrieves the subscription status for a specific approver.
   *
   * @param approver the email or identifier of the approver
   * @return the subscription status for the given approver
   */
  AnimalInfoNotifStatus getSubscriptionStatus(@NonNull String approver);

  /**
   * Handles the approval or rejection of a subscription request.
   *
   * @param email the email of the subscriber
   * @param approver the approver handling the subscription request
   * @param reject true if the subscription should be rejected, false if approved
   */
  void handleSubscriptionApproval(@NonNull String email, String approver, boolean reject);
}
