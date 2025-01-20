package com.ansh.management.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ansh.auth.service.UserProfileService;
import com.ansh.dto.SubscriptionRequest;
import com.ansh.entity.animal.UserProfile.AnimalNotificationSubscriptionStatus;
import com.ansh.entity.subscription.Subscription;
import com.ansh.repository.entity.PendingSubscriber;
import com.ansh.management.service.AnimalTopicPendingSubscriptionService;
import com.ansh.management.service.NotificationSubscriptionService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class SubscriptionControllerTest {

  @Mock
  private NotificationSubscriptionService notificationSubscriptionService;

  @Mock
  private AnimalTopicPendingSubscriptionService animalTopicPendingSubscriptionService;

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
  void shouldApproveSubscriber() {
    doNothing().when(animalTopicPendingSubscriptionService).approveSubscriber(anyString(), anyString());

    subscriptionController.approve(subscriptionRequest);

    verify(animalTopicPendingSubscriptionService, times(1)).approveSubscriber(
        subscriptionRequest.getEmail(), subscriptionRequest.getApprover());
  }

  @Test
  void shouldRejectSubscriber() {
    doNothing().when(animalTopicPendingSubscriptionService).rejectSubscriber(anyString());

    subscriptionController.reject(subscriptionRequest);

    verify(animalTopicPendingSubscriptionService, times(1)).rejectSubscriber(
        subscriptionRequest.getEmail());
  }

  @Test
  void shouldReturnPendingSubscribers() {
    PendingSubscriber subscriber = new PendingSubscriber();
    List<PendingSubscriber> subscribers = List.of(subscriber);
    when(animalTopicPendingSubscriptionService.getSubscribersByApprover(anyString())).thenReturn(subscribers);

    List<PendingSubscriber> result = subscriptionController.getPendingSubscribers(
        subscriptionRequest);

    assertNotNull(result);
    assertEquals(1, result.size());
    verify(animalTopicPendingSubscriptionService, times(1))
        .getSubscribersByApprover(subscriptionRequest.getApprover());
  }

  @Test
  void shouldReturnPendingNoApproverSubscribers() {
    PendingSubscriber subscriber = new PendingSubscriber();
    List<PendingSubscriber> subscribers = List.of(subscriber);
    when(animalTopicPendingSubscriptionService.getPendingSubscribersWithoutApprover())
        .thenReturn(subscribers);

    List<PendingSubscriber> result = subscriptionController.getPendingNoApproverSubscribers();

    assertNotNull(result);
    assertEquals(1, result.size());
    verify(animalTopicPendingSubscriptionService, times(1))
        .getPendingSubscribersWithoutApprover();
  }

  @Test
  void shouldReturnSubscribers() {
    Subscription subscription = new Subscription();
    List<Subscription> subscriptions = List.of(subscription);
    when(notificationSubscriptionService.getAllSubscriptionByApprover(anyString())).thenReturn(subscriptions);

    List<Subscription> result = subscriptionController.getSubscribers(subscriptionRequest);

    assertNotNull(result);
    assertEquals(1, result.size());
    verify(notificationSubscriptionService, times(1)).getAllSubscriptionByApprover(
        subscriptionRequest.getApprover());
  }

  @Test
  void shouldReturnApproverStatus() {
    AnimalNotificationSubscriptionStatus status = AnimalNotificationSubscriptionStatus.ACTIVE;
    when(notificationSubscriptionService.getStatusByApprover(anyString())).thenReturn(status);
    doNothing().when(userProfileService).updateNotificationStatusOfAuthUser(status);

    AnimalNotificationSubscriptionStatus result = subscriptionController.getApproverStatus(
        subscriptionRequest);

    assertEquals(status, result);
    verify(notificationSubscriptionService, times(1)).getStatusByApprover(subscriptionRequest.getApprover());
    verify(userProfileService, times(1)).updateNotificationStatusOfAuthUser(status);
  }
}
