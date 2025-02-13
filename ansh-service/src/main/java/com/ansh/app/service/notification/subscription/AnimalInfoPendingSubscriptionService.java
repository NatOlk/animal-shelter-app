package com.ansh.app.service.notification.subscription;

import com.ansh.app.service.exception.user.UnauthorizedActionException;
import com.ansh.repository.entity.PendingSubscriber;
import java.util.List;

/**
 * Service for managing pending subscriptions to animal notifications.
 */
public interface AnimalInfoPendingSubscriptionService {

  /**
   * Approves a pending subscriber, granting them access to notifications.
   *
   * @param email the email of the subscriber to approve
   * @param approver the email or identifier of the approver
   * @throws UnauthorizedActionException if the approver is not authorized to approve the subscription
   */
  void approveSubscriber(String email, String approver) throws UnauthorizedActionException;

  /**
   * Rejects a pending subscriber, denying them access to notifications.
   *
   * @param email the email of the subscriber to reject
   * @param approver the email or identifier of the approver
   * @throws UnauthorizedActionException if the approver is not authorized to reject the subscription
   */
  void rejectSubscriber(String email, String approver) throws UnauthorizedActionException;

  /**
   * Saves a new pending subscriber request.
   *
   * @param email the email of the subscriber requesting approval
   * @param approver the email or identifier of the person responsible for approving the request
   */
  void saveSubscriber(String email, String approver);

  /**
   * Retrieves a list of pending subscribers that require approval from a specific approver.
   *
   * @param approver the email or identifier of the approver
   * @return a list of {@link PendingSubscriber} objects awaiting approval from the specified approver
   */
  List<PendingSubscriber> getSubscribersByApprover(String approver);

  /**
   * Retrieves a list of pending subscribers who do not have an assigned approver.
   *
   * @return a list of {@link PendingSubscriber} objects awaiting an approver
   */
  List<PendingSubscriber> getPendingSubscribersWithoutApprover();
}
