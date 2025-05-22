package com.ansh.app.service.notification.subscription;

import com.ansh.entity.account.UserProfile.AnimalInfoNotifStatus;
import com.ansh.dto.NotificationStatusDTO;
import com.ansh.entity.subscription.Subscription;
import java.util.List;
import reactor.core.publisher.Mono;

/**
 * Service for managing notification subscriptions.
 * <p>
 * This service uses Project Reactor's {@link Mono} to handle asynchronous,
 * non-blocking operations.
 * </p>
 */
public interface NotificationSubscriptionService {

  /**
   * Retrieves all subscriptions approved by a specific account.
   * <p>
   * The result is wrapped in a {@link Mono}, meaning it will be processed asynchronously
   * and will emit either the list of {@link Subscription} objects or an empty list if
   * no subscriptions are found.
   * </p>
   *
   * @param email the identifier of the account
   * @return a {@link Mono} emitting a list of {@link Subscription} objects
   */
  Mono<List<Subscription>> getAllSubscriptionByAccount(String email);

  /**
   * Retrieves the notification subscription statuses for a given account.
   * <p>
   * The method returns a {@link Mono}, which emits either the current
   * {@link AnimalInfoNotifStatus} or an empty signal if
   * no status is found.
   * </p>
   *
   * @param email the identifier of the account
   * @return a {@link Mono} emitting the {@link AnimalInfoNotifStatus}
   */
  Mono<NotificationStatusDTO> getStatusesByAccount(String email);

  /**
   * Registers a new subscription request.
   * <p>
   * This method attempts to register a user ({@code email}) for notifications
   * under a specific {@code topic}, optionally approved by a designated {@code approver}.
   * The {@code approver} may be {@code null} or empty if the topic does not require explicit approval.
   * The operation is performed asynchronously and returns a boolean indicating success.
   * </p>
   *
   * @param email    the email of the user requesting the subscription
   * @param approver the account responsible for approving the subscription (optional)
   * @param topic    the topic for which the user is subscribing
   * @return a {@link Mono} emitting {@code true} if the subscription was successfully registered,
   *         or {@code false} in case of error or rejection
   */
  Mono<Boolean> registerSubscriber(String email, String approver, String topic);

  /**
   * Unsubscribes a user from a specific topic.
   * <p>
   * This method removes the subscription for the given {@code email}, {@code topic},
   * and {@code approver}. The result is emitted asynchronously and indicates whether
   * the operation succeeded.
   * </p>
   *
   * @param email    the email of the user unsubscribing
   * @param approver the account that approved the subscription
   * @param topic    the topic from which the user wants to unsubscribe
   * @return a {@link Mono} emitting {@code true} if the unsubscription was successful,
   *         or {@code false} if an error occurred
   */
  Mono<Boolean> unsubscribe(String email, String approver, String topic);
}
