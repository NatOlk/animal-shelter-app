package com.ansh.app.service.notification.subscription.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ansh.entity.account.UserProfile.AnimalInfoNotifStatus;
import com.ansh.entity.subscription.Subscription;
import com.ansh.notification.external.ExternalNotificationServiceClient;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class NotificationSubscriptionServiceTest {

  @InjectMocks
  private NotificationSubscriptionServiceImpl notificationSubscriptionService;

  @Mock
  private ExternalNotificationServiceClient externalNotificationServiceClient;

  private final String allByApproverEndpoint = "/internal/animal-notify-all-approver-subscriptions";
  private final String statusByApproverEndpoint = "/internal/animal-notify-approver-status";

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    notificationSubscriptionService.setAllByApproverEndpoint(allByApproverEndpoint);
    notificationSubscriptionService.setStatusByApproverEndpoint(statusByApproverEndpoint);
  }

  @Test
  void shouldReturnAllSubscriptionsByApprover() {
    String approver = "approver@example.com";

    Subscription s1 = new Subscription();
    s1.setId(1L);
    s1.setTopic("topic1");

    Subscription s2 = new Subscription();
    s2.setId(2L);
    s2.setTopic("topic2");

    List<Subscription> mockSubscriptions = List.of(s1, s2);

    when(externalNotificationServiceClient.post(
        eq(allByApproverEndpoint),
        eq(Map.of("approver", approver)),
        eq(new ParameterizedTypeReference<List<Subscription>>() {})
    )).thenReturn(Mono.just(mockSubscriptions));

    StepVerifier.create(notificationSubscriptionService.getAllSubscriptionByApprover(approver))
        .assertNext(result -> {
          assertNotNull(result);
          assertEquals(2, result.size());
          assertEquals("topic1", result.get(0).getTopic());
          assertEquals("topic2", result.get(1).getTopic());
        })
        .verifyComplete();

    verify(externalNotificationServiceClient).post(
        eq(allByApproverEndpoint),
        eq(Map.of("approver", approver)),
        eq(new ParameterizedTypeReference<List<Subscription>>() {})
    );
  }

  @Test
  void shouldReturnEmptyList_whenServiceFails() {
    String approver = "approver@example.com";

    when(externalNotificationServiceClient.post(
        eq(allByApproverEndpoint),
        eq(Map.of("approver", approver)),
        eq(new ParameterizedTypeReference<List<Subscription>>() {})
    )).thenReturn(Mono.error(new RuntimeException("Service unavailable")));

    StepVerifier.create(notificationSubscriptionService.getAllSubscriptionByApprover(approver))
        .assertNext(result -> {
          assertNotNull(result);
          assertEquals(0, result.size());
        })
        .verifyComplete();

    verify(externalNotificationServiceClient).post(
        eq(allByApproverEndpoint),
        eq(Map.of("approver", approver)),
        eq(new ParameterizedTypeReference<List<Subscription>>() {})
    );
  }

  @Test
  void shouldReturnStatusByApprover() {
    String approver = "approver@example.com";
    AnimalInfoNotifStatus expectedStatus = AnimalInfoNotifStatus.ACTIVE;

    when(externalNotificationServiceClient.post(
        eq(statusByApproverEndpoint),
        eq(Map.of("approver", approver)),
        eq(new ParameterizedTypeReference<AnimalInfoNotifStatus>() {})
    )).thenReturn(Mono.just(expectedStatus));

    StepVerifier.create(notificationSubscriptionService.getAnimalInfoStatusByApprover(approver))
        .assertNext(result -> {
          assertNotNull(result);
          assertEquals(expectedStatus, result);
        })
        .verifyComplete();

    verify(externalNotificationServiceClient).post(
        eq(statusByApproverEndpoint),
        eq(Map.of("approver", approver)),
        eq(new ParameterizedTypeReference<AnimalInfoNotifStatus>() {})
    );
  }

  @Test
  void shouldReturnUnknownStatus_whenServiceFails() {
    String approver = "approver@example.com";

    when(externalNotificationServiceClient.post(
        eq(statusByApproverEndpoint),
        eq(Map.of("approver", approver)),
        eq(new ParameterizedTypeReference<AnimalInfoNotifStatus>() {})
    )).thenReturn(Mono.error(new RuntimeException("Service unavailable")));

    StepVerifier.create(notificationSubscriptionService.getAnimalInfoStatusByApprover(approver))
        .assertNext(result -> {
          assertNotNull(result);
          assertEquals(AnimalInfoNotifStatus.UNKNOWN, result);
        })
        .verifyComplete();

    verify(externalNotificationServiceClient).post(
        eq(statusByApproverEndpoint),
        eq(Map.of("approver", approver)),
        eq(new ParameterizedTypeReference<AnimalInfoNotifStatus>() {})
    );
  }

  @Test
  void shouldHandleEmptyListFromService() {
    String approver = "approver@example.com";

    when(externalNotificationServiceClient.post(
        eq(allByApproverEndpoint),
        eq(Map.of("approver", approver)),
        eq(new ParameterizedTypeReference<List<Subscription>>() {})
    )).thenReturn(Mono.just(Collections.emptyList()));

    StepVerifier.create(notificationSubscriptionService.getAllSubscriptionByApprover(approver))
        .assertNext(result -> {
          assertNotNull(result);
          assertEquals(0, result.size());
        })
        .verifyComplete();

    verify(externalNotificationServiceClient).post(
        eq(allByApproverEndpoint),
        eq(Map.of("approver", approver)),
        eq(new ParameterizedTypeReference<List<Subscription>>() {})
    );
  }
}
