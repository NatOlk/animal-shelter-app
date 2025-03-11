package com.ansh.app.controller;

import com.ansh.app.service.exception.user.UnauthorizedActionException;
import com.ansh.app.service.notification.subscription.NotificationSubscriptionService;
import com.ansh.app.service.notification.subscription.PendingSubscriptionService;
import com.ansh.app.service.user.impl.UserProfileServiceImpl;
import com.ansh.dto.SubscriptionRequest;
import com.ansh.entity.animal.UserProfile.AnimalInfoNotifStatus;
import com.ansh.entity.subscription.Subscription;
import com.ansh.repository.entity.PendingSubscriber;
import io.micrometer.common.util.StringUtils;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
@RequestMapping("/api")
public class SubscriptionController {

  @Autowired
  @Qualifier("notificationSubscriptionService")
  private NotificationSubscriptionService notificationService;

  @Autowired
  @Qualifier("animalInfoPendingSubscriptionService")
  private PendingSubscriptionService pendingSubscriptionService;

  @Autowired
  private UserProfileServiceImpl userProfileService;

  @PostMapping("/animal-notify-approve-subscriber")
  public void approve(@RequestBody SubscriptionRequest req) throws UnauthorizedActionException {
    if (StringUtils.isEmpty(req.getApprover()) || StringUtils.isEmpty(
        req.getEmail())) {
      return;
    }
    pendingSubscriptionService.approveSubscriber(req.getEmail(), req.getApprover());
  }

  @PostMapping("/animal-notify-reject-subscriber")
  public void reject(@RequestBody SubscriptionRequest req) throws UnauthorizedActionException {
    if (StringUtils.isEmpty(req.getApprover()) || StringUtils.isEmpty(
        req.getEmail())) {
      return;
    }
    pendingSubscriptionService.rejectSubscriber(req.getEmail(), req.getApprover());
  }

  @PostMapping(value = "/animal-notify-pending-subscribers")
  public List<PendingSubscriber> getPendingSubscribers(
      @RequestBody SubscriptionRequest subscriptionRequest) {
    if (StringUtils.isEmpty(subscriptionRequest.getApprover())) {
      return Collections.emptyList();
    }
    return pendingSubscriptionService.getSubscribersByApprover(subscriptionRequest.getApprover());
  }

  @GetMapping("/animal-notify-pending-no-approver-subscribers")
  public List<PendingSubscriber> getPendingNoApproverSubscribers() {
    return pendingSubscriptionService.getPendingSubscribersWithoutApprover();
  }

  @PostMapping("/animal-notify-all-approver-subscriptions")
  public DeferredResult<List<Subscription>> getSubscribers(
      @RequestBody SubscriptionRequest subscriptionRequest) {
    DeferredResult<List<Subscription>> output = new DeferredResult<>();

    if (StringUtils.isEmpty(subscriptionRequest.getApprover())) {
      output.setResult(Collections.emptyList());
      return output;
    }

    notificationService.getAllSubscriptionByApprover(subscriptionRequest.getApprover())
        .subscribe(
            output::setResult,
            error -> output.setResult(Collections.emptyList())
        );

    return output;
  }

  @PostMapping("/animal-notify-approver-status")
  public DeferredResult<AnimalInfoNotifStatus> getApproverStatus(
      @RequestBody SubscriptionRequest subscriptionRequest) {

    DeferredResult<AnimalInfoNotifStatus> output = new DeferredResult<>();

    if (StringUtils.isEmpty(subscriptionRequest.getApprover())) {
      output.setResult(AnimalInfoNotifStatus.NONE);
      return output;
    }

    notificationService.getAnimalInfoStatusByApprover(subscriptionRequest.getApprover())
        .subscribe(
            status -> {
              userProfileService.updateNotificationStatusOfAuthUser(status);
              output.setResult(status);
            },
            error -> output.setResult(AnimalInfoNotifStatus.UNKNOWN)
        );

    return output;
  }
}
