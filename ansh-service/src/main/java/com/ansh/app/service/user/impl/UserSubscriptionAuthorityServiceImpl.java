package com.ansh.app.service.user.impl;

import com.ansh.app.service.exception.user.UnauthorizedActionException;
import com.ansh.app.service.user.UserSubscriptionAuthorityService;
import com.ansh.auth.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserSubscriptionAuthorityServiceImpl implements UserSubscriptionAuthorityService {

  @Autowired
  private UserProfileRepository userProfileRepository;

  @Override
  public void checkAuthorityToApprove(String approver) throws UnauthorizedActionException {
    if ((approver == null || approver.isEmpty()) ||
        !userProfileRepository.hasRole(approver, "ADMIN")) {
      throw new UnauthorizedActionException("You do not have permission to approve requests.");
    }
  }

  @Override
  public void checkAuthorityToReject(String approver) throws UnauthorizedActionException {
    if ((approver == null || approver.isEmpty()) ||
        !userProfileRepository.hasRole(approver, "ADMIN")) {
      throw new UnauthorizedActionException("You do not have permission to reject requests.");
    }
  }

  @Override
  public void checkAuthorityToUnsubscribe(String approver) throws UnauthorizedActionException {
    if ((approver == null || approver.isEmpty()) ||
        !userProfileRepository.hasRole(approver, "ADMIN")) {
      throw new UnauthorizedActionException("You do not have permission to unsubscribe requests.");
    }
  }
}
