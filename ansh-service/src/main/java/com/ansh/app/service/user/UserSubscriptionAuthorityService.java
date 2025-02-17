package com.ansh.app.service.user;

import com.ansh.app.service.exception.user.UnauthorizedActionException;

/**
 * Service interface for verifying an approver's authority
 * to approve, reject, or unsubscribe a user from a subscription.
 */
public interface UserSubscriptionAuthorityService {

  /**
   * Verifies if the given approver has the necessary authority
   * to approve a subscription request.
   *
   * @param approver the identifier of the approver
   * @throws UnauthorizedActionException if the approver lacks the required permission
   */
  void checkAuthorityToApprove(String approver) throws UnauthorizedActionException;

  /**
   * Verifies if the given approver has the necessary authority
   * to reject a subscription request.
   *
   * @param approver the identifier of the approver
   * @throws UnauthorizedActionException if the approver lacks the required permission
   */
  void checkAuthorityToReject(String approver) throws UnauthorizedActionException;

  /**
   * Verifies if the given approver has the necessary authority
   * to unsubscribe a user from a subscription.
   *
   * @param approver the identifier of the approver
   * @throws UnauthorizedActionException if the approver lacks the required permission
   */
  void checkAuthorityToUnsubscribe(String approver) throws UnauthorizedActionException;
}