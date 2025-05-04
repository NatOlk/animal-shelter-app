package com.ansh.facade;

import com.ansh.dto.SubscriptionRequest;
import com.ansh.entity.account.UserProfile.AnimalInfoNotifStatus;
import com.ansh.entity.subscription.Subscription;
import java.util.List;
import java.util.Optional;

public interface SubscriptionFacade {

  //TODO: add specs
  List<Subscription> getAllSubscriptionByApprover(String approver);

  void registerEmployeeSubscription(SubscriptionRequest req);

  void unregisterEmployeeSubscription(String token);
  Optional<AnimalInfoNotifStatus> getSubscriptionStatus(SubscriptionRequest req);
}
