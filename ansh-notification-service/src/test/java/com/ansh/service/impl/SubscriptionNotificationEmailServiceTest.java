package com.ansh.service.impl;

import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ansh.entity.subscription.Subscription;
import com.ansh.service.EmailService;
import com.ansh.utils.LinkGenerator;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class SubscriptionNotificationEmailServiceTest {

  @Mock
  private EmailService emailService;

  @InjectMocks
  private SubscriptionNotificationEmailServiceImpl subscriptionNotificationService;

  @Mock
  private LinkGenerator linkGenerator;

  private final String TEST_TOKEN = "test-token";

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    when(linkGenerator.generateUnsubscribeLink(TEST_TOKEN)).thenReturn("http://localhost:8081/external/animal-notify-unsubscribe/" + TEST_TOKEN);
    when(linkGenerator.generateConfirmationLink(TEST_TOKEN)).thenReturn("http://localhost:8081/external/animal-notify-subscribe-check/" + TEST_TOKEN);
  }

  @Test
  void testSendNeedAcceptSubscriptionEmail() {
    Subscription subscription = new Subscription();
    subscription.setEmail("test@example.com");
    subscription.setToken(TEST_TOKEN);

    subscriptionNotificationService.sendNeedAcceptSubscriptionEmail(subscription);

    Map<String, Object> expectedParams = Map.of(
        "name", "test@example.com",
        "unsubscribeLink", "http://localhost:8081/external/animal-notify-unsubscribe/test-token",
        "confirmationLink", "http://localhost:8081/external/animal-notify-subscribe-check/test-token",
        "subscriptionLink", "http://localhost:8081/external/animal-notify-subscribe-check/test-token"
    );

    verify(emailService).sendEmail(
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

    verify(emailService).sendEmail(
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
    subscription.setToken(TEST_TOKEN);

    subscriptionNotificationService.sendRepeatConfirmationEmail(subscription);

    Map<String, Object> expectedParams = Map.of(
        "name", "test@example.com",
        "unsubscribeLink", "http://localhost:8081/external/animal-notify-unsubscribe/test-token"
    );

    verify(emailService).sendEmail(
        eq("test@example.com"),
        eq("[animal-shelter-app] Subscription for Animal Shelter app"),
        eq("repeatSubscription"),
        eq(expectedParams)
    );
  }
}
