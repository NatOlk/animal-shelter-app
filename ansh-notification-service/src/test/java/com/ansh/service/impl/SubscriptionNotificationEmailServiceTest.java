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

  private static final String EMAIL = "test@example.com";
  private static final String TEST_TOKEN = "test-token";
  private static final String UNSUBSCRIBE_LINK =
      "http://localhost:8081/external/animal-notify-unsubscribe/" + TEST_TOKEN;
  private static final String CONFIRMATION_LINK =
      "http://localhost:8081/external/animal-notify-subscribe-check/" + TEST_TOKEN;

  private static final String SUBJECT_ACCEPT = "[animal-shelter-app] Please accept subscription for Animal Shelter News";
  private static final String SUBJECT_SUCCESS = "[animal-shelter-app] Subscription for Animal Shelter News";

  private static final String TEMPLATE_ACCEPT = "acceptSubscription";
  private static final String TEMPLATE_SUCCESS = "successSubscription";
  private static final String TEMPLATE_REPEAT = "repeatSubscription";

  @Mock
  private EmailService emailService;

  @Mock
  private LinkGenerator linkGenerator;

  @InjectMocks
  private SubscriptionNotificationEmailServiceImpl subscriptionNotificationService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    when(linkGenerator.generateUnsubscribeLink(TEST_TOKEN)).thenReturn(UNSUBSCRIBE_LINK);
    when(linkGenerator.generateConfirmationLink(TEST_TOKEN)).thenReturn(CONFIRMATION_LINK);
  }

  @Test
  void testSendNeedAcceptSubscriptionEmail() {
    // given
    Subscription subscription = new Subscription();
    subscription.setEmail(EMAIL);
    subscription.setToken(TEST_TOKEN);

    Map<String, Object> expectedParams = Map.of(
        "name", EMAIL,
        "unsubscribeLink", UNSUBSCRIBE_LINK,
        "confirmationLink", CONFIRMATION_LINK,
        "subscriptionLink", CONFIRMATION_LINK
    );

    // when
    subscriptionNotificationService.sendNeedAcceptSubscriptionEmail(subscription);

    // then
    verify(emailService).sendEmail(
        eq(EMAIL),
        eq(SUBJECT_ACCEPT),
        eq(TEMPLATE_ACCEPT),
        eq(expectedParams)
    );
  }

  @Test
  void testSendSuccessTokenConfirmationEmail() {
    // given
    Subscription subscription = new Subscription();
    subscription.setEmail(EMAIL);
    subscription.setToken(TEST_TOKEN);

    Map<String, Object> expectedParams = Map.of(
        "name", EMAIL,
        "unsubscribeLink", UNSUBSCRIBE_LINK
    );

    // when
    subscriptionNotificationService.sendSuccessTokenConfirmationEmail(subscription);

    // then
    verify(emailService).sendEmail(
        eq(EMAIL),
        eq(SUBJECT_SUCCESS),
        eq(TEMPLATE_SUCCESS),
        eq(expectedParams)
    );
  }

  @Test
  void testSendRepeatConfirmationEmail() {
    // given
    Subscription subscription = new Subscription();
    subscription.setEmail(EMAIL);
    subscription.setToken(TEST_TOKEN);

    Map<String, Object> expectedParams = Map.of(
        "name", EMAIL,
        "unsubscribeLink", UNSUBSCRIBE_LINK
    );

    // when
    subscriptionNotificationService.sendRepeatConfirmationEmail(subscription);

    // then
    verify(emailService).sendEmail(
        eq(EMAIL),
        eq(SUBJECT_SUCCESS),
        eq(TEMPLATE_REPEAT),
        eq(expectedParams)
    );
  }
}