package com.ansh.app.facade;

import com.ansh.dto.SubscriptionRequest;
import com.ansh.entity.account.UserProfile.AnimalInfoNotifStatus;
import com.ansh.entity.subscription.Subscription;
import java.util.List;
import org.springframework.web.context.request.async.DeferredResult;

public interface SubscriptionFacade {

  //TODO: add documentation
  DeferredResult<AnimalInfoNotifStatus> getApproverStatus(SubscriptionRequest subscriptionRequest);
//TODO: add spec
  DeferredResult<List<Subscription>> getAllSubscribers(SubscriptionRequest subscriptionRequest);
}
