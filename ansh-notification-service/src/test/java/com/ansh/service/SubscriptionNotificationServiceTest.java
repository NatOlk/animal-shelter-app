package com.ansh.service;

import static org.mockito.Mockito.*;

import com.ansh.entity.subscription.Subscription;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class SubscriptionNotificationServiceTest {

  @Mock
  private EmailService emailService;

  @InjectMocks
  private SubscriptionNotificationService subscriptionNotificationService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    subscriptionNotificationService.setAnimalShelterNotificationApp("http://localhost:8081");
  }

  @Test
  void testSendNeedAcceptSubscriptionEmail() {
    Subscription subscription = new Subscription();
    subscription.setEmail("test@example.com");
    subscription.setToken("test-token");

    subscriptionNotificationService.sendNeedAcceptSubscriptionEmail(subscription);

    Map<String, Object> expectedParams = Map.of(
        "name", "test@example.com",
        "unsubscribeLink", "http://localhost:8081/external/animal-notify-unsubscribe/test-token",
        "confirmationLink", "http://localhost:8081/external/animal-notify-subscribe-check/test-token",
        "subscriptionLink", "http://localhost:8081/external/animal-notify-subscribe-check/test-token"
    );

    verify(emailService).sendSimpleMessage(
        eq("test@example.com"),
        eq("[animal-shelter-app] Please accept subscription for Animal Shelter app"),
        eq("acceptSubscription"),
        eq(expectedParams)
    );
  }

  @Test
  void testSendSuccessTokenConfirmationEmail() {
    Subscription subscription = new Subscription();
    subscription.setEmail("test@example.com");
    subscription.setToken("test-token");

    subscriptionNotificationService.sendSuccessTokenConfirmationEmail(subscription);

    Map<String, Object> expectedParams = Map.of(
        "name", "test@example.com",
        "unsubscribeLink", "http://localhost:8081/external/animal-notify-unsubscribe/test-token"
    );

    verify(emailService).sendSimpleMessage(
        eq("test@example.com"),
        eq("[animal-shelter-app] Subscription for Animal Shelter app"),
        eq("successSubscription"),
        eq(expectedParams)
    );
  }

  @Test
  void testSendRepeatConfirmationEmail() {
    Subscription subscription = new Subscription();
    subscription.setEmail("test@example.com");
    subscription.setToken("test-token");

    subscriptionNotificationService.sendRepeatConfirmationEmail(subscription);

    Map<String, Object> expectedParams = Map.of(
        "name", "test@example.com",
        "unsubscribeLink", "http://localhost:8081/external/animal-notify-unsubscribe/test-token"
    );

    verify(emailService).sendSimpleMessage(
        eq("test@example.com"),
        eq("[animal-shelter-app] Subscription for Animal Shelter app"),
        eq("repeatSubscription"),
        eq(expectedParams)
    );
  }
}
