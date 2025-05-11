package com.ansh.app.facade.impl;

import com.ansh.app.facade.SubscriptionFacade;
import com.ansh.app.service.notification.subscription.NotificationSubscriptionService;
import com.ansh.app.service.user.UserProfileService;
import com.ansh.dto.NotificationStatusDTO;
import com.ansh.dto.SubscriptionRequest;
import com.ansh.entity.account.UserProfile.AnimalInfoNotifStatus;
import com.ansh.entity.subscription.Subscription;
import io.micrometer.common.util.StringUtils;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

@Component
public class SubscriptionFacadeImpl implements SubscriptionFacade {

  private static final Logger LOG = LoggerFactory.getLogger(SubscriptionFacadeImpl.class);

  @Autowired
  @Qualifier("notificationSubscriptionService")
  private NotificationSubscriptionService notificationService;

  @Autowired
  private UserProfileService userProfileService;

  @Override
  public DeferredResult<NotificationStatusDTO> getNotificationStatusesByAccount(String email) {
    DeferredResult<NotificationStatusDTO> output = new DeferredResult<>();

    LOG.debug("Fetching notification statuses for approver: {}", email);

    if (StringUtils.isEmpty(email)) {
      LOG.debug("Email is empty, returning NONE statuses.");
      output.setResult(new NotificationStatusDTO(
          AnimalInfoNotifStatus.NONE,
          AnimalInfoNotifStatus.NONE,
          AnimalInfoNotifStatus.NONE
      ));
      return output;
    }

    notificationService.getStatusesByAccount(email)
        .subscribe(
            statusDto -> {
              LOG.debug("Received statuses for {}: {}", email, statusDto);
              userProfileService.updateNotificationStatusOfAuthUser(statusDto);
              output.setResult(statusDto);
            },
            error -> {
              LOG.warn("Failed to fetch statuses for {}: {}", email, error.getMessage());
              output.setResult(new NotificationStatusDTO(
                  AnimalInfoNotifStatus.NONE,
                  AnimalInfoNotifStatus.NONE,
                  AnimalInfoNotifStatus.NONE
              ));
            }
        );

    return output;
  }

  @Override
  public DeferredResult<List<Subscription>> getAllSubscribers(String email) {
    DeferredResult<List<Subscription>> output = new DeferredResult<>();
    LOG.debug("Fetching all subscribers for approver: {}", email);

    if (StringUtils.isEmpty(email)) {
      LOG.debug("Email is empty, returning empty list.");
      output.setResult(Collections.emptyList());
      return output;
    }

    notificationService.getAllSubscriptionByAccount(email)
        .subscribe(
            subscriptions -> {
              LOG.debug("Fetched {} subscriptions for approver {}", subscriptions.size(), email);
              output.setResult(subscriptions);
            },
            error -> {
              LOG.warn("Failed to fetch subscriptions for {}: {}", email, error.getMessage());
              output.setResult(Collections.emptyList());
            }
        );

    return output;
  }

  @Override
  public void registerSubscription(SubscriptionRequest req) {
    LOG.debug("Registering subscription: {}", req);

    if (StringUtils.isEmpty(req.getTopic()) || StringUtils.isEmpty(req.getEmail())) {
      LOG.warn("Subscription request missing required fields: {}", req);
      return;
    }
    notificationService.registerSubscriber(req.getEmail(), req.getApprover(), req.getTopic());
  }

  @Override
  public void unsubscribe(SubscriptionRequest req) {
    LOG.debug("Unregistering {} for topic: {}", req.getEmail(), req.getTopic());
    notificationService.unsubscribe(req.getEmail(), req.getApprover(), req.getTopic());
  }
}
