package com.ansh.facade;

import com.ansh.dto.NotificationStatusDTO;
import com.ansh.dto.SubscriptionRequest;
import com.ansh.entity.subscription.Subscription;
import java.util.List;

/**
 * Facade interface for managing subscription-related operations.
 * Provides methods to register, retrieve, and remove subscriptions,
 * as well as to query current notification statuses for a user.
 */
public interface SubscriptionFacade {

  /**
   * Retrieves all subscriptions where the specified user is the account.
   *
   * @param approver the email of the approver user
   * @return a list of {@link Subscription} objects associated with the approver
   */
  List<Subscription> getAllSubscriptionByAccount(String approver);

  /**
   * Registers a new subscription based on the provided request.
   *
   * @param req the {@link SubscriptionRequest} containing subscription details
   */
  void registerSubscription(SubscriptionRequest req);

  /**
   * Unsubscribes a user using the unique token (typically from email link).
   *
   * @param token the subscription token used for unsubscription
   */
  void unsubscribe(String token);

  /**
   * Unsubscribes a user based on the email and topic provided in the request.
   *
   * @param req the {@link SubscriptionRequest} containing email and topic
   */
  void unsubscribe(SubscriptionRequest req);

  /**
   * Handles the approval or rejection of a subscription request.
   *
   * @param email the email address of the user who requested the subscription
   * @param approver the email address of the approver (e.g. an admin or moderator)
   * @param topic the topic to which the subscription applies
   * @param reject if {@code true}, the subscription is rejected; if {@code false}, it is approved
   *
   * @throws IllegalArgumentException if the topic is not supported or required information is missing
   */
  void handleSubscriptionApproval(String email, String approver, String topic, boolean reject);

  /**
   * Retrieves the current subscription statuses for a given account (email).
   * Returns statuses for each supported topic.
   *
   * @param account the email address of the account to query
   * @return a {@link NotificationStatusDTO} with statuses per topic
   */
  NotificationStatusDTO getSubscriptionStatuses(String account);
}
