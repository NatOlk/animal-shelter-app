package com.ansh.app.service.user.impl;

import com.ansh.app.service.exception.user.UnauthorizedActionException;
import com.ansh.app.service.user.UserSubscriptionAuthorityService;
import org.springframework.stereotype.Service;

@Service
public class UserSubscriptionAuthorityServiceImpl implements UserSubscriptionAuthorityService {

  @Override
  public void checkAuthorityToApprove(String approver) throws UnauthorizedActionException {
    // throw new UnauthorizedActionException("You do not have permission to approve requests.");
  }

  @Override
  public void checkAuthorityToReject(String approver) throws UnauthorizedActionException {
   // throw new UnauthorizedActionException("You do not have permission to reject requests.");
  }

  @Override
  public void checkAuthorityToUnsubscribe(String approver) throws UnauthorizedActionException {
   // throw new UnauthorizedActionException("You do not have permission to unsubscribe requests.");
  }
}
