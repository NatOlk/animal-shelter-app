package com.ansh.app.facade.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import com.ansh.app.service.notification.subscription.NotificationSubscriptionService;
import com.ansh.app.service.user.UserProfileService;
import com.ansh.dto.SubscriptionRequest;
import com.ansh.entity.account.UserProfile.AnimalInfoNotifStatus;
import com.ansh.dto.NotificationStatusDTO;
import com.ansh.entity.subscription.Subscription;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import reactor.core.publisher.Mono;

class SubscriptionFacadeImplTest {

  private static final String APPROVER_EMAIL = "admin@example.com";
  private static final String TOPIC = "animalTopicId";

  @InjectMocks
  private SubscriptionFacadeImpl subscriptionFacade;

  @Mock
  private NotificationSubscriptionService notificationService;

  @Mock
  private UserProfileService userProfileService;

  private SubscriptionRequest request;

  @BeforeEach
  void setUp() {
    openMocks(this);
    request = SubscriptionRequest.builder()
        .approver(APPROVER_EMAIL)
        .topic(TOPIC)
        .build();
  }

  @Test
  void shouldReturnActiveStatus() throws InterruptedException {
    // given
    NotificationStatusDTO expected = new NotificationStatusDTO(
        AnimalInfoNotifStatus.NONE,
        AnimalInfoNotifStatus.ACTIVE,
        AnimalInfoNotifStatus.NONE
    );

    when(notificationService.getStatusesByAccount(APPROVER_EMAIL))
        .thenReturn(Mono.just(expected));

    // when
    var result = subscriptionFacade.getNotificationStatusesByAccount(request.getApprover());

    // then
    CountDownLatch latch = new CountDownLatch(1);
    result.onCompletion(latch::countDown);
    latch.await(1, TimeUnit.SECONDS);

    assertEquals(expected, result.getResult());
  }

  @Test
  void shouldReturnUnknownStatusOnError() throws InterruptedException {
    // given
    when(notificationService.getStatusesByAccount(APPROVER_EMAIL))
        .thenReturn(Mono.error(new RuntimeException("Failed")));

    // when
    var result = subscriptionFacade.getNotificationStatusesByAccount(request.getApprover());

    // then
    CountDownLatch latch = new CountDownLatch(1);
    result.onCompletion(latch::countDown);
    latch.await(1, TimeUnit.SECONDS);

    assertEquals(new NotificationStatusDTO(), result.getResult());
  }

  @Test
  void shouldReturnNoneStatusIfApproverIsEmpty() {
    // given
    request.setApprover(null);

    // when
    var result = subscriptionFacade.getNotificationStatusesByAccount(request.getApprover());

    // then
    assertEquals(new NotificationStatusDTO(), result.getResult());
  }

  @Test
  void shouldReturnAllSubscribers() throws InterruptedException {
    // given
    List<Subscription> subscriptions = List.of(new Subscription());

    when(notificationService.getAllSubscriptionByAccount(APPROVER_EMAIL))
        .thenReturn(Mono.just(subscriptions));

    // when
    var result = subscriptionFacade.getAllSubscribers(request.getApprover());

    // then
    CountDownLatch latch = new CountDownLatch(1);
    result.onCompletion(latch::countDown);
    latch.await(1, TimeUnit.SECONDS);

    assertEquals(subscriptions, result.getResult());
  }

  @Test
  void shouldReturnEmptyListOnError() throws InterruptedException {
    // given
    when(notificationService.getAllSubscriptionByAccount(APPROVER_EMAIL))
        .thenReturn(Mono.error(new RuntimeException("DB error")));

    // when
    var result = subscriptionFacade.getAllSubscribers(request.getApprover());

    // then
    CountDownLatch latch = new CountDownLatch(1);
    result.onCompletion(latch::countDown);
    latch.await(1, TimeUnit.SECONDS);

    assertEquals(Collections.emptyList(), result.getResult());
  }
}