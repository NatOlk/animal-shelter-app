package com.ansh.uimanagement.controller;

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
import com.ansh.uimanagement.service.AnimalTopicSubscriptionService;
import com.ansh.uimanagement.service.SubscriptionService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class SubscriptionControllerTest {

  @Mock
  private SubscriptionService subscriptionService;

  @Mock
  private AnimalTopicSubscriptionService animalTopicSubscriptionService;

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
    doNothing().when(animalTopicSubscriptionService).approveSubscriber(anyString(), anyString());

    subscriptionController.approve(subscriptionRequest);

    verify(animalTopicSubscriptionService, times(1)).approveSubscriber(
        subscriptionRequest.getEmail(), subscriptionRequest.getApprover());
  }

  @Test
  void shouldRejectSubscriber() {
    doNothing().when(animalTopicSubscriptionService).rejectSubscriber(anyString());

    subscriptionController.reject(subscriptionRequest);

    verify(animalTopicSubscriptionService, times(1)).rejectSubscriber(
        subscriptionRequest.getEmail());
  }

  @Test
  void shouldReturnPendingSubscribers() {
    PendingSubscriber subscriber = new PendingSubscriber();
    List<PendingSubscriber> subscribers = List.of(subscriber);
    when(subscriptionService.getPendingSubscribers(anyString())).thenReturn(subscribers);

    List<PendingSubscriber> result = subscriptionController.getPendingSubscribers(
        subscriptionRequest);

    assertNotNull(result);
    assertEquals(1, result.size());
    verify(subscriptionService, times(1)).getPendingSubscribers(subscriptionRequest.getApprover());
  }

  @Test
  void shouldReturnPendingNoApproverSubscribers() {
    PendingSubscriber subscriber = new PendingSubscriber();
    List<PendingSubscriber> subscribers = List.of(subscriber);
    when(subscriptionService.getPendingNoApproverSubscribers()).thenReturn(subscribers);

    List<PendingSubscriber> result = subscriptionController.getPendingNoApproverSubscribers();

    assertNotNull(result);
    assertEquals(1, result.size());
    verify(subscriptionService, times(1)).getPendingNoApproverSubscribers();
  }

  @Test
  void shouldReturnSubscribers() {
    Subscription subscription = new Subscription();
    List<Subscription> subscriptions = List.of(subscription);
    when(subscriptionService.getAllSubscriptionByApprover(anyString())).thenReturn(subscriptions);

    List<Subscription> result = subscriptionController.getSubscribers(subscriptionRequest);

    assertNotNull(result);
    assertEquals(1, result.size());
    verify(subscriptionService, times(1)).getAllSubscriptionByApprover(
        subscriptionRequest.getApprover());
  }

  @Test
  void shouldReturnApproverStatus() {
    AnimalNotificationSubscriptionStatus status = AnimalNotificationSubscriptionStatus.ACTIVE;
    when(subscriptionService.getStatusByApprover(anyString())).thenReturn(status);
    doNothing().when(userProfileService).updateNotificationStatusOfAuthUser(status);

    AnimalNotificationSubscriptionStatus result = subscriptionController.getApproverStatus(
        subscriptionRequest);

    assertEquals(status, result);
    verify(subscriptionService, times(1)).getStatusByApprover(subscriptionRequest.getApprover());
    verify(userProfileService, times(1)).updateNotificationStatusOfAuthUser(status);
  }
}
