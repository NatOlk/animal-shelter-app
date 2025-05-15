package com.ansh.controller;

import com.ansh.dto.NotificationStatusDTO;
import com.ansh.dto.SubscriptionRequest;
import com.ansh.entity.subscription.Subscription;
import com.ansh.facade.SubscriptionFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Internal Subscription Controller", description = "Handles internal subscription operations via API key.")
public class SubscriptionController {

  @Autowired
  private SubscriptionFacade subscriptionFacade;

  @PostMapping("/internal/subscription/all")
  @Operation(
      summary = "Get all subscriptions for account",
      description = "Returns all subscription entries associated with the provided approver email.",
      parameters = {
          @Parameter(name = "X-API-KEY", description = "Internal API key", required = true, in = ParameterIn.HEADER)
      },
      requestBody = @RequestBody(
          required = true,
          content = @Content(schema = @Schema(implementation = SubscriptionRequest.class))
      ),
      responses = @ApiResponse(responseCode = "200", description = "List of subscriptions")
  )
  public List<Subscription> allSubscriptionsByAccount(
      @org.springframework.web.bind.annotation.RequestBody SubscriptionRequest request) {
    return subscriptionFacade.getAllSubscriptionByAccount(request.getApprover());
  }

  @PostMapping("/internal/subscription/statuses")
  @Operation(
      summary = "Get subscription statuses for account",
      description = "Returns current statuses of all notification types for the provided approver email.",
      parameters = {
          @Parameter(name = "X-API-KEY", description = "Internal API key", required = true, in = ParameterIn.HEADER)
      },
      requestBody = @RequestBody(
          required = true,
          content = @Content(schema = @Schema(implementation = SubscriptionRequest.class))
      ),
      responses = @ApiResponse(responseCode = "200", description = "Notification status DTO")
  )
  public NotificationStatusDTO getStatusesByAccount(
      @org.springframework.web.bind.annotation.RequestBody SubscriptionRequest request) {
    return subscriptionFacade.getSubscriptionStatuses(request.getApprover());
  }

  @PostMapping("/internal/subscription/register")
  @Operation(
      summary = "Register internal subscription",
      description = "Registers a new internal subscription for the employee account.",
      parameters = {
          @Parameter(name = "X-API-KEY", description = "Internal API key", required = true, in = ParameterIn.HEADER)
      },
      requestBody = @RequestBody(
          required = true,
          content = @Content(schema = @Schema(implementation = SubscriptionRequest.class))
      ),
      responses = @ApiResponse(responseCode = "200", description = "Subscription registered")
  )
  public void registerEmployeeSubscription(
      @org.springframework.web.bind.annotation.RequestBody SubscriptionRequest request) {
    subscriptionFacade.registerSubscription(request);
  }

  @PostMapping("/internal/subscription/unsubscribe")
  @Operation(
      summary = "Unsubscribe internal user",
      description = "Removes the subscription for the specified account.",
      parameters = {
          @Parameter(name = "X-API-KEY", description = "Internal API key", required = true, in = ParameterIn.HEADER)
      },
      requestBody = @RequestBody(
          required = true,
          content = @Content(schema = @Schema(implementation = SubscriptionRequest.class))
      ),
      responses = @ApiResponse(responseCode = "200", description = "Subscription removed")
  )
  public String unsubscribe(
      @org.springframework.web.bind.annotation.RequestBody SubscriptionRequest request) {
    subscriptionFacade.unsubscribe(request);
    return "Subscription is removed";
  }
}
