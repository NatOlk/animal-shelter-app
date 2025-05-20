package com.ansh.app.facade;

import com.ansh.repository.entity.PendingSubscriber;
import java.util.List;

/**
 * Facade interface for managing pending subscription requests.
 * Provides methods to approve or reject individual subscription requests,
 * and to retrieve pending subscribers by approver or without assigned approver.
 */
public interface PendingSubscriptionFacade {

  /**
   * Approves a pending subscription request.
   *
   * @param topic    the topic the user is trying to subscribe to
   * @param email    the email of the subscriber
   * @param approver the email or ID of the user approving the subscription
   */
  void approveSubscriber(String topic, String email, String approver);

  /**
   * Rejects a pending subscription request.
   *
   * @param topic    the topic the user is trying to subscribe to
   * @param email    the email of the subscriber
   * @param approver the email or ID of the user rejecting the subscription
   */
  void rejectSubscriber(String topic, String email, String approver);

  /**
   * Retrieves a list of all pending subscribers assigned to a specific approver.
   *
   * @param account the identifier (e.g., email or username) of the approver
   * @return list of pending subscribers assigned to the given approver
   */
  List<PendingSubscriber> getSubscribersByApprover(String account);

  /**
   * Retrieves all pending subscribers that are not yet assigned to any approver.
   *
   * @return list of unassigned pending subscribers
   */
  List<PendingSubscriber> getPendingSubscribersWithoutApprover();
}