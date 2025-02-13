package com.ansh.app.service.user;

import com.ansh.app.service.exception.user.UnauthorizedActionException;

/**
 * Service interface for checking user authorization for different actions.
 */
public interface UserAuthorityService {

  /**
   * Checks if the given approver has the authority to approve a request.
   *
   * @param approver the identifier of the approver
   * @throws UnauthorizedActionException if the approver does not have the required permission
   */
  void checkAuthorityToApprove(String approver) throws UnauthorizedActionException;

  /**
   * Checks if the given approver has the authority to reject a request.
   *
   * @param approver the identifier of the approver
   * @throws UnauthorizedActionException if the approver does not have the required permission
   */
  void checkAuthorityToReject(String approver) throws UnauthorizedActionException;

  /**
   * Checks if the given approver has the authority to unsubscribe a user from a service.
   *
   * @param approver the identifier of the approver
   * @throws UnauthorizedActionException if the approver does not have the required permission
   */
  void checkAuthorityToUnsubscribe(String approver) throws UnauthorizedActionException;
}
