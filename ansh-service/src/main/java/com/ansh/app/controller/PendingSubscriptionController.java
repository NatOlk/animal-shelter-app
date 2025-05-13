package com.ansh.app.controller;

import com.ansh.app.service.exception.user.UnauthorizedActionException;
import com.ansh.dto.SubscriptionRequest;
import com.ansh.notification.strategy.PendingSubscriptionServiceStrategy;
import com.ansh.repository.entity.PendingSubscriber;
import io.micrometer.common.util.StringUtils;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PendingSubscriptionController {

  @Autowired
  private PendingSubscriptionServiceStrategy pendingSubscriptionServiceStrategy;

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/subscription/approve")
  public void approve(@RequestBody SubscriptionRequest req) throws UnauthorizedActionException {
    if (StringUtils.isEmpty(req.getApprover()) || StringUtils.isEmpty(req.getEmail()) ||
        StringUtils.isEmpty(req.getTopic())) {
      return;
    }
    var service = pendingSubscriptionServiceStrategy.getServiceByTopic(req.getTopic())
        .orElseThrow(() -> new IllegalArgumentException(
            STR."No service found for topic: \{req.getTopic()}"));

    service.approveSubscriber(req.getEmail(), req.getApprover());
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/subscription/reject")
  public void reject(@RequestBody SubscriptionRequest req) throws UnauthorizedActionException {
    if (StringUtils.isEmpty(req.getApprover()) || StringUtils.isEmpty(req.getEmail()) ||
        StringUtils.isEmpty(req.getTopic())) {
      return;
    }

    var service = pendingSubscriptionServiceStrategy.getServiceByTopic(req.getTopic())
        .orElseThrow(() -> new IllegalArgumentException(
            STR."No service found for topic: \{req.getTopic()}"));

    service.rejectSubscriber(req.getEmail(), req.getApprover());
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping(value = "/subscription/pending-subscribers")
  public List<PendingSubscriber> getPendingSubscribers(@RequestBody SubscriptionRequest req) {
    if (StringUtils.isEmpty(req.getApprover())) {
      return Collections.emptyList();
    }
    return pendingSubscriptionServiceStrategy.getAllServices().stream()
        .flatMap(service -> service.getSubscribersByApprover(req.getApprover()).stream())
        .toList();
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/subscription/no-approver-subscribers")
  public List<PendingSubscriber> getPendingNoApproverSubscribers() {
    return pendingSubscriptionServiceStrategy.getAllServices().stream()
        .flatMap(service -> service.getPendingSubscribersWithoutApprover().stream())
        .toList();
  }
}
