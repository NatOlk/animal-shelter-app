package com.ansh.app.controller;

import com.ansh.app.facade.PendingSubscriptionFacade;
import com.ansh.dto.SubscriptionRequest;
import com.ansh.repository.entity.PendingSubscriber;
import io.micrometer.common.util.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/api/subscription/pending")
@Tag(name = "Pending Subscription", description = "Endpoints for managing pending subscription requests")
@SecurityRequirement(name = "bearerAuth")
public class PendingSubscriptionController {

  @Autowired
  private PendingSubscriptionFacade pendingSubscriptionFacade;

  @Operation(summary = "Approve subscription request",
      description = "Approve a pending subscription for a given email and topic. Requires ADMIN role.")
  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/approve")
  public void approve(@RequestBody SubscriptionRequest req) {
    if (StringUtils.isEmpty(req.getApprover()) || StringUtils.isEmpty(req.getEmail()) ||
        StringUtils.isEmpty(req.getTopic())) {
      return;
    }
    pendingSubscriptionFacade.approveSubscriber(req.getTopic(), req.getEmail(), req.getApprover());
  }

  @Operation(summary = "Reject subscription request",
      description = "Reject a pending subscription for a given email and topic. Requires ADMIN role.")
  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/reject")
  public void reject(@RequestBody SubscriptionRequest req) {
    if (StringUtils.isEmpty(req.getApprover()) || StringUtils.isEmpty(req.getEmail()) ||
        StringUtils.isEmpty(req.getTopic())) {
      return;
    }

    pendingSubscriptionFacade.rejectSubscriber(req.getTopic(), req.getEmail(), req.getApprover());
  }

  @Operation(summary = "Get pending subscribers for approver",
      description = "Returns a list of pending subscribers assigned to the provided approver. Requires ADMIN role.")
  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/subscribers")
  public List<PendingSubscriber> getPendingSubscribers(@RequestBody SubscriptionRequest req) {
    if (StringUtils.isEmpty(req.getApprover())) {
      return Collections.emptyList();
    }
    return pendingSubscriptionFacade.getSubscribersByApprover(req.getApprover());
  }

  @Operation(summary = "Get pending subscribers without approver",
      description = "Returns a list of pending subscribers who don't have an approver assigned. Requires ADMIN role.")
  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/no-approver-subscribers")
  public List<PendingSubscriber> getPendingNoApproverSubscribers() {
    return pendingSubscriptionFacade.getPendingSubscribersWithoutApprover();
  }
}