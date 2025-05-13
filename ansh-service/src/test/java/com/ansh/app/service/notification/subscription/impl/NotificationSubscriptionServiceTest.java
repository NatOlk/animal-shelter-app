package com.ansh.app.service.notification.subscription.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.ansh.dto.NotificationStatusDTO;
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

  private static final String APPROVER = "approver@example.com";
  private static final String ALL_BY_APPROVER_ENDPOINT = "/internal/animal-notify-all-approver-subscriptions";
  private static final String STATUS_BY_APPROVER_ENDPOINT = "/internal/animal-notify-approver-status";

  @InjectMocks
  private NotificationSubscriptionServiceImpl notificationSubscriptionService;

  @Mock
  private ExternalNotificationServiceClient externalNotificationServiceClient;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    notificationSubscriptionService.setAllEndpoint(ALL_BY_APPROVER_ENDPOINT);
    notificationSubscriptionService.setStatusesEndpoint(STATUS_BY_APPROVER_ENDPOINT);
  }

  @Test
  void shouldReturnAllSubscriptionsByApprover() {
    // given
    Subscription s1 = new Subscription();
    s1.setId(1L);
    s1.setTopic("topic1");

    Subscription s2 = new Subscription();
    s2.setId(2L);
    s2.setTopic("topic2");

    List<Subscription> mockSubscriptions = List.of(s1, s2);

    when(externalNotificationServiceClient.post(
        eq(ALL_BY_APPROVER_ENDPOINT),
        eq(Map.of("approver", APPROVER)),
        eq(new ParameterizedTypeReference<List<Subscription>>() {
        })
    )).thenReturn(Mono.just(mockSubscriptions));

    // when + then
    StepVerifier.create(notificationSubscriptionService.getAllSubscriptionByAccount(APPROVER))
        .assertNext(result -> {
          assertNotNull(result);
          assertEquals(2, result.size());
          assertEquals("topic1", result.get(0).getTopic());
          assertEquals("topic2", result.get(1).getTopic());
        })
        .verifyComplete();
  }

  @Test
  void shouldReturnEmptyList_whenServiceFails() {
    // given
    when(externalNotificationServiceClient.post(
        eq(ALL_BY_APPROVER_ENDPOINT),
        eq(Map.of("approver", APPROVER)),
        eq(new ParameterizedTypeReference<List<Subscription>>() {
        })
    )).thenReturn(Mono.error(new RuntimeException("Service unavailable")));

    // when + then
    StepVerifier.create(notificationSubscriptionService.getAllSubscriptionByAccount(APPROVER))
        .assertNext(result -> {
          assertNotNull(result);
          assertEquals(0, result.size());
        })
        .verifyComplete();
  }

  @Test
  void shouldReturnStatusDtoByApprover() {
    // given
    NotificationStatusDTO expected = new NotificationStatusDTO(
        AnimalInfoNotifStatus.ACTIVE,
        AnimalInfoNotifStatus.PENDING,
        AnimalInfoNotifStatus.NONE
    );

    when(externalNotificationServiceClient.post(
        eq(STATUS_BY_APPROVER_ENDPOINT),
        eq(Map.of("approver", APPROVER)),
        eq(new ParameterizedTypeReference<NotificationStatusDTO>() {
        })
    )).thenReturn(Mono.just(expected));

    // when + then
    StepVerifier.create(notificationSubscriptionService.getStatusesByAccount(APPROVER))
        .assertNext(result -> {
          assertNotNull(result);
          assertEquals(expected.animalShelterNewsTopicId(), result.animalShelterNewsTopicId());
          assertEquals(expected.animalTopicId(), result.animalTopicId());
          assertEquals(expected.vaccinationTopicId(), result.vaccinationTopicId());
        })
        .verifyComplete();
  }

  @Test
  void shouldReturnUnknownDto_whenServiceFails() {
    // given
    when(externalNotificationServiceClient.post(
        eq(STATUS_BY_APPROVER_ENDPOINT),
        eq(Map.of("approver", APPROVER)),
        eq(new ParameterizedTypeReference<NotificationStatusDTO>() {
        })
    )).thenReturn(Mono.error(new RuntimeException("Service unavailable")));

    // when + then
    StepVerifier.create(notificationSubscriptionService.getStatusesByAccount(APPROVER))
        .assertNext(result -> {
          assertNotNull(result);
          assertEquals(new NotificationStatusDTO(AnimalInfoNotifStatus.UNKNOWN,
              AnimalInfoNotifStatus.UNKNOWN, AnimalInfoNotifStatus.UNKNOWN), result);
        })
        .verifyComplete();
  }

  @Test
  void shouldHandleEmptyListFromService() {
    // given
    when(externalNotificationServiceClient.post(
        eq(ALL_BY_APPROVER_ENDPOINT),
        eq(Map.of("approver", APPROVER)),
        eq(new ParameterizedTypeReference<List<Subscription>>() {
        })
    )).thenReturn(Mono.just(Collections.emptyList()));

    // when + then
    StepVerifier.create(notificationSubscriptionService.getAllSubscriptionByAccount(APPROVER))
        .assertNext(result -> {
          assertNotNull(result);
          assertEquals(0, result.size());
        })
        .verifyComplete();
  }
}