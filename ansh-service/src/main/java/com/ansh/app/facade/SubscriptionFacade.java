package com.ansh.app.facade;

import com.ansh.dto.NotificationStatusDTO;
import com.ansh.dto.SubscriptionRequest;
import com.ansh.entity.subscription.Subscription;
import java.util.List;
import org.springframework.web.context.request.async.DeferredResult;
import reactor.core.publisher.Mono;

/**
 * Facade for managing subscription-related operations in the application layer. Provides methods to
 * handle subscription registration, unsubscription, and querying statuses.
 */
public interface SubscriptionFacade {

  /**
   * Retrieves all notification subscription statuses for a given user account. Used to determine
   * the current state of subscriptions (ACTIVE, PENDING, etc.) for all available topics.
   *
   * @param email the email of the account to retrieve statuses for
   * @return a {@link DeferredResult} containing a {@link NotificationStatusDTO} with per-topic
   * subscription statuses
   */
  DeferredResult<NotificationStatusDTO> getNotificationStatusesByAccount(String email);

  /**
   * Retrieves all subscriptions associated with the given user (as approver).
   *
   * @param email the email of the approver
   * @return a {@link DeferredResult} containing a list of {@link Subscription} entities
   */
  DeferredResult<List<Subscription>> getAllSubscribers(String email);

  /**
   * Registers a new subscription based on the provided request. Intended for internal use by
   * services to subscribe employees or admins to notifications. Returns a {@link Mono} containing
   * {@code true} if the operation was successful, or {@code false} if an error occurred.
   *
   * @param req the {@link SubscriptionRequest} containing email, topic, and optional approver
   * @return {@link Mono<Boolean>} indicating success or failure
   */
  Mono<Boolean> registerSubscription(SubscriptionRequest req);

  /**
   * Cancels a subscription based on the provided request. Returns a {@link Mono} containing
   * {@code true} if the operation was successful, or {@code false} if an error occurred.
   *
   * @param req the {@link SubscriptionRequest} containing email, topic, and optional approver
   * @return {@link Mono<Boolean>} indicating success or failure
   */
  Mono<Boolean> unsubscribe(SubscriptionRequest req);
}
