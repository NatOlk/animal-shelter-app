package com.ansh.controller;

import com.ansh.dto.SubscriptionRequest;
import com.ansh.service.SubscriberRegistryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for managing subscriptions **only** to Animal Shelter News notifications.
 * This controller handles:
 * - New subscription requests
 * - Unsubscription by token
 * - Subscription confirmation by token
 * Note: It specifically works **only with the Animal Shelter News topic**.
 */
@RestController
@Tag(name = "Animal Shelter News Subscription", description = "Endpoints for managing subscription to shelter news.")
public class AnimalShelterNewsSubscriptionController {

  @Autowired
  @Qualifier("animalShelterNewsSubscriber")
  private SubscriberRegistryService animalShelterNewsSubscriber;

  @Operation(
      summary = "Send subscription request",
      description = "Initiates a subscription request to receive animal shelter news notifications."
  )
  @ApiResponse(responseCode = "200", description = "Subscription request sent")
  @PostMapping("/external/animal-notify-subscribe")
  public void subscribe(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
          required = true,
          content = @Content(schema = @Schema(implementation = SubscriptionRequest.class))
      )
      @RequestBody SubscriptionRequest request
  ) {
    String email = request.getEmail();
    if (email == null || email.isEmpty()) {
      return;
    }
    email = email.replace("\"", "");

    String approver = request.getApprover();
    if (approver == null) {
      approver = "";
    }
    approver = approver.replace("\"", "");
    animalShelterNewsSubscriber.registerSubscriber(email, approver);
  }

  @Operation(
      summary = "Unsubscribe from notifications",
      description = "Removes a subscription using the provided token."
  )
  @ApiResponse(responseCode = "200", description = "Subscription removed")
  @GetMapping("/external/animal-notify-unsubscribe/{token}")
  public String unsubscribe(@PathVariable String token) {
    animalShelterNewsSubscriber.unsubscribe(token);
    return "Subscription is removed";
  }

  @Operation(
      summary = "Confirm subscription via token",
      description = "Verifies and accepts a subscription using a unique token (usually from email)."
  )
  @ApiResponse(responseCode = "200", description = "Subscription status returned")
  @GetMapping("/external/animal-notify-subscribe-check/{token}")
  public String checkSubscription(@PathVariable String token) {
    boolean isAccepted = animalShelterNewsSubscriber.acceptSubscription(token);
    return STR."Your subscription is \{isAccepted ? "accepted" : "not accepted"}";
  }
}