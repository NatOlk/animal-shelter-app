package com.ansh.service;

import com.ansh.entity.subscription.Subscription;

/**
 * Service interface for sending subscription-related email notifications.
 */
public interface SubscriptionNotificationEmailService {

  /**
   * Sends an email to notify the subscriber that they need to accept the subscription.
   *
   * @param sb the subscription details containing subscriber information
   */
  void sendNeedAcceptSubscriptionEmail(Subscription sb);

  /**
   * Sends an email confirming that the subscription token has been successfully verified.
   *
   * @param sb the subscription details containing subscriber information
   */
  void sendSuccessTokenConfirmationEmail(Subscription sb);

  /**
   * Sends a repeat confirmation email if the subscriber did not confirm their subscription
   * initially.
   *
   * @param sb the subscription details containing subscriber information
   */
  void sendRepeatConfirmationEmail(Subscription sb);
}
