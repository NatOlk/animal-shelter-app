package com.ansh.app.service.notification.subscription;

import com.ansh.entity.account.UserProfile.AnimalInfoNotifStatus;
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
   * Retrieves all subscriptions approved by a specific approver.
   * <p>
   * The result is wrapped in a {@link Mono}, meaning it will be processed asynchronously
   * and will emit either the list of {@link Subscription} objects or an empty list if
   * no subscriptions are found.
   * </p>
   *
   * @param approver the identifier of the approver
   * @return a {@link Mono} emitting a list of {@link Subscription} objects
   */
  Mono<List<Subscription>> getAllSubscriptionByApprover(String approver);

  /**
   * Retrieves the animal info subscription status for a given approver.
   * <p>
   * The method returns a {@link Mono}, which emits either the current
   * {@link AnimalInfoNotifStatus} or an empty signal if
   * no status is found.
   * </p>
   *
   * @param approver the identifier of the approver
   * @return a {@link Mono} emitting the {@link AnimalInfoNotifStatus}
   */
  Mono<AnimalInfoNotifStatus> getAnimalInfoStatusByApprover(String approver);
}
