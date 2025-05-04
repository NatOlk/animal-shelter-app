package com.ansh.app.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ansh.app.service.exception.user.UnauthorizedActionException;
import com.ansh.app.service.notification.subscription.NotificationSubscriptionService;
import com.ansh.app.service.notification.subscription.PendingSubscriptionService;
import com.ansh.app.service.user.UserProfileService;
import com.ansh.dto.SubscriptionRequest;
import com.ansh.entity.account.UserProfile.AnimalInfoNotifStatus;
import com.ansh.entity.subscription.Subscription;
import com.ansh.repository.entity.PendingSubscriber;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.context.request.async.DeferredResult;
import reactor.core.publisher.Mono;

class SubscriptionControllerTest {

  @Mock
  private NotificationSubscriptionService notificationSubscriptionService;

  @Mock
  private PendingSubscriptionService animalInfoPendingSubscriptionService;

  @Mock
  private UserProfileService userProfileService;

  @InjectMocks
  private SubscriptionController subscriptionController;

  private SubscriptionRequest subscriptionRequest;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    subscriptionRequest = new SubscriptionRequest();
    subscriptionRequest.setApprover("testApprover");
    subscriptionRequest.setEmail("test@example.com");
  }

  @Test
  void shouldApproveSubscriber() throws UnauthorizedActionException {
    doNothing().when(animalInfoPendingSubscriptionService)
        .approveSubscriber(anyString(), anyString());

    subscriptionController.approve(subscriptionRequest);

    verify(animalInfoPendingSubscriptionService, times(1)).approveSubscriber(
        subscriptionRequest.getEmail(), subscriptionRequest.getApprover());
  }

  @Test
  void shouldRejectSubscriber() throws UnauthorizedActionException {
    doNothing().when(animalInfoPendingSubscriptionService)
        .rejectSubscriber(anyString(), anyString());

    subscriptionController.reject(subscriptionRequest);

    verify(animalInfoPendingSubscriptionService, times(1)).rejectSubscriber(
        subscriptionRequest.getEmail(), subscriptionRequest.getApprover());
  }

  @Test
  void shouldReturnPendingSubscribers() {
    PendingSubscriber subscriber = new PendingSubscriber();
    List<PendingSubscriber> subscribers = List.of(subscriber);
    when(animalInfoPendingSubscriptionService.getSubscribersByApprover(anyString()))
        .thenReturn(
            subscribers);

    List<PendingSubscriber> result = subscriptionController.getPendingSubscribers(
        subscriptionRequest);

    assertNotNull(result);
    assertEquals(1, result.size());
    verify(animalInfoPendingSubscriptionService, times(1))
        .getSubscribersByApprover(subscriptionRequest.getApprover());
  }

  @Test
  void shouldReturnPendingNoApproverSubscribers() {
    PendingSubscriber subscriber = new PendingSubscriber();
    List<PendingSubscriber> subscribers = List.of(subscriber);
    when(animalInfoPendingSubscriptionService.getPendingSubscribersWithoutApprover())
        .thenReturn(subscribers);

    List<PendingSubscriber> result = subscriptionController.getPendingNoApproverSubscribers();

    assertNotNull(result);
    assertEquals(1, result.size());
    verify(animalInfoPendingSubscriptionService, times(1))
        .getPendingSubscribersWithoutApprover();
  }

  @Test
  void shouldReturnSubscribers() throws InterruptedException {
    Subscription subscription = new Subscription();
    List<Subscription> subscriptions = List.of(subscription);
    when(notificationSubscriptionService.getAllSubscriptionByApprover(anyString()))
        .thenReturn(Mono.just(subscriptions));

    DeferredResult<List<Subscription>> deferredResult =
        subscriptionController.getSubscribers(subscriptionRequest);

    CountDownLatch latch = new CountDownLatch(1);
    deferredResult.onCompletion(latch::countDown);
    latch.await(1, TimeUnit.SECONDS);

    assertNotNull(deferredResult.getResult());
    assertEquals(subscriptions, deferredResult.getResult());

    verify(notificationSubscriptionService, times(1)).getAllSubscriptionByApprover(
        subscriptionRequest.getApprover());
  }

  @Test
  void shouldReturnEmptyList_whenServiceFails() throws InterruptedException {
    when(notificationSubscriptionService.getAllSubscriptionByApprover(anyString()))
        .thenReturn(Mono.error(new RuntimeException("Service unavailable")));

    DeferredResult<List<Subscription>> deferredResult =
        subscriptionController.getSubscribers(subscriptionRequest);

    CountDownLatch latch = new CountDownLatch(1);
    deferredResult.onCompletion(latch::countDown);
    latch.await(1, TimeUnit.SECONDS);

    assertNotNull(deferredResult.getResult());
    assertEquals(Collections.emptyList(), deferredResult.getResult());

    verify(notificationSubscriptionService, times(1)).getAllSubscriptionByApprover(
        subscriptionRequest.getApprover());
  }

  @Test
  void shouldReturnApproverStatus() throws InterruptedException {
    AnimalInfoNotifStatus status = AnimalInfoNotifStatus.ACTIVE;
    when(notificationSubscriptionService.getAnimalInfoStatusByApprover(anyString())).thenReturn(
        Mono.just(status));
    doNothing().when(userProfileService).updateNotificationStatusOfAuthUser(status);

    DeferredResult<AnimalInfoNotifStatus> deferredResult =
        subscriptionController.getApproverStatus(subscriptionRequest);

    CountDownLatch latch = new CountDownLatch(1);
    deferredResult.onCompletion(latch::countDown);
    latch.await(1, TimeUnit.SECONDS);

    assertNotNull(deferredResult.getResult());
    assertEquals(status, deferredResult.getResult());

    verify(notificationSubscriptionService, times(1))
        .getAnimalInfoStatusByApprover(subscriptionRequest.getApprover());
    verify(userProfileService, times(1)).updateNotificationStatusOfAuthUser(status);
  }

  @Test
  void shouldReturnUnknownStatus_whenServiceFails() throws InterruptedException {
    when(notificationSubscriptionService.getAnimalInfoStatusByApprover(anyString()))
        .thenReturn(Mono.error(new RuntimeException("Service unavailable")));

    DeferredResult<AnimalInfoNotifStatus> deferredResult =
        subscriptionController.getApproverStatus(subscriptionRequest);

    CountDownLatch latch = new CountDownLatch(1);
    deferredResult.onCompletion(latch::countDown);
    latch.await(1, TimeUnit.SECONDS);

    assertNotNull(deferredResult.getResult());
    assertEquals(AnimalInfoNotifStatus.UNKNOWN, deferredResult.getResult());

    verify(notificationSubscriptionService, times(1)).getAnimalInfoStatusByApprover(
        subscriptionRequest.getApprover());
  }

  @Test
  void shouldHandleEmptyApproverGracefully() throws InterruptedException {
    subscriptionRequest.setApprover("");

    DeferredResult<List<Subscription>> deferredResult =
        subscriptionController.getSubscribers(subscriptionRequest);

    CountDownLatch latch = new CountDownLatch(1);
    deferredResult.onCompletion(latch::countDown);
    latch.await(1, TimeUnit.SECONDS);

    assertNotNull(deferredResult.getResult());
    assertEquals(Collections.emptyList(), deferredResult.getResult());
  }
}
