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

  void registerSubscriber(String email, String approver, String topic);
  void unsubscribe(String email, String approver, String topic);
}
