package com.ansh.app.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.ansh.app.service.exception.user.UnauthorizedActionException;
import com.ansh.auth.service.UserProfileService;
import com.ansh.dto.SubscriptionRequest;
import com.ansh.entity.animal.UserProfile.AnimalNotificationSubscriptionStatus;
import com.ansh.entity.subscription.Subscription;
import com.ansh.app.service.notification.subscription.AnimalInfoPendingSubscriptionService;
import com.ansh.app.service.notification.subscription.NotificationSubscriptionService;
import com.ansh.repository.entity.PendingSubscriber;
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
  private AnimalInfoPendingSubscriptionService animalInfoPendingSubscriptionService;

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
    doNothing().when(animalInfoPendingSubscriptionService).approveSubscriber(anyString(), anyString());

    subscriptionController.approve(subscriptionRequest);

    verify(animalInfoPendingSubscriptionService, times(1)).approveSubscriber(
        subscriptionRequest.getEmail(), subscriptionRequest.getApprover());
  }

  @Test
  void shouldRejectSubscriber() throws UnauthorizedActionException {
    doNothing().when(animalInfoPendingSubscriptionService).rejectSubscriber(anyString(), anyString());

    subscriptionController.reject(subscriptionRequest);

    verify(animalInfoPendingSubscriptionService, times(1)).rejectSubscriber(
        subscriptionRequest.getEmail(), subscriptionRequest.getApprover());
  }

  @Test
  void shouldReturnPendingSubscribers() {
    PendingSubscriber subscriber = new PendingSubscriber();
    List<PendingSubscriber> subscribers = List.of(subscriber);
    when(animalInfoPendingSubscriptionService.getSubscribersByApprover(anyString())).thenReturn(subscribers);

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
  void shouldReturnApproverStatus() throws InterruptedException {
    AnimalNotificationSubscriptionStatus status = AnimalNotificationSubscriptionStatus.ACTIVE;
    when(notificationSubscriptionService.getStatusByApprover(anyString())).thenReturn(Mono.just(status));
    doNothing().when(userProfileService).updateNotificationStatusOfAuthUser(status);

    DeferredResult<AnimalNotificationSubscriptionStatus> deferredResult =
        subscriptionController.getApproverStatus(subscriptionRequest);

    CountDownLatch latch = new CountDownLatch(1);
    deferredResult.onCompletion(latch::countDown);
    latch.await(1, TimeUnit.SECONDS);

    assertNotNull(deferredResult.getResult());
    assertEquals(status, deferredResult.getResult());

    verify(notificationSubscriptionService, times(1)).getStatusByApprover(subscriptionRequest.getApprover());
    verify(userProfileService, times(1)).updateNotificationStatusOfAuthUser(status);
  }
}
