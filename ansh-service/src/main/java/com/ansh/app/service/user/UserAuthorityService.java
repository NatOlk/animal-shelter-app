package com.ansh.app.service.user;

import com.ansh.app.service.exception.user.UnauthorizedActionException;

public interface UserAuthorityService {

  void checkAuthorityToApprove(String approver) throws UnauthorizedActionException;
  void checkAuthorityToReject(String approver) throws UnauthorizedActionException;

  void checkAuthorityToUnsubscribe(String approver) throws UnauthorizedActionException;
}
