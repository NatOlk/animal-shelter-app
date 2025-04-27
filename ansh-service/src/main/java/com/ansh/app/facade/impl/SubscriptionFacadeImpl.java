package com.ansh.app.facade.impl;

import com.ansh.app.facade.SubscriptionFacade;
import com.ansh.app.service.notification.subscription.NotificationSubscriptionService;
import com.ansh.app.service.user.UserProfileService;
import com.ansh.dto.SubscriptionRequest;
import com.ansh.entity.account.UserProfile.AnimalInfoNotifStatus;
import com.ansh.entity.subscription.Subscription;
import io.micrometer.common.util.StringUtils;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

@Component
public class SubscriptionFacadeImpl implements SubscriptionFacade {

  @Autowired
  @Qualifier("notificationSubscriptionService")
  private NotificationSubscriptionService notificationService;

  @Autowired
  private UserProfileService userProfileService;

  @Override
  public DeferredResult<AnimalInfoNotifStatus> getApproverStatus(SubscriptionRequest req) {
    DeferredResult<AnimalInfoNotifStatus> output = new DeferredResult<>();

    if (StringUtils.isEmpty(req.getApprover())) {
      output.setResult(AnimalInfoNotifStatus.NONE);
      return output;
    }

    notificationService.getAnimalInfoStatusByApprover(req.getApprover())
        .subscribe(
            status -> {
              userProfileService.updateNotificationStatusOfAuthUser(status);
              output.setResult(status);
            },
            error -> output.setResult(AnimalInfoNotifStatus.UNKNOWN)
        );

    return output;
  }

  @Override
  public DeferredResult<List<Subscription>> getAllSubscribers(SubscriptionRequest req) {
    DeferredResult<List<Subscription>> output = new DeferredResult<>();
    if (StringUtils.isEmpty(req.getApprover())) {
      output.setResult(Collections.emptyList());
      return output;
    }

    notificationService.getAllSubscriptionByApprover(req.getApprover())
        .subscribe(
            output::setResult,
            error -> output.setResult(Collections.emptyList())
        );

    return output;
  }
}
