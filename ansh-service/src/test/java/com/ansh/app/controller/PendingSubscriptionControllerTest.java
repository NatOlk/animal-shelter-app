package com.ansh.app.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ansh.app.facade.SubscriptionFacade;
import com.ansh.app.service.notification.subscription.PendingSubscriptionService;
import com.ansh.dto.NotificationStatusDTO;
import com.ansh.dto.SubscriptionRequest;
import com.ansh.entity.account.UserProfile.AnimalInfoNotifStatus;
import com.ansh.entity.subscription.Subscription;
import com.ansh.notification.strategy.PendingSubscriptionServiceStrategy;
import com.ansh.repository.entity.PendingSubscriber;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.context.request.async.DeferredResult;

class PendingSubscriptionControllerTest {

  private static final String APPROVER_EMAIL = "admin@example.com";
  private static final String TEST_EMAIL = "test@example.com";
  private static final String TOPIC_ID = "animalTopicId";

  @Mock
  private PendingSubscriptionServiceStrategy pendingSubscriptionServiceStrategy;

  @Mock
  private PendingSubscriptionService animalInfoPendingSubscriptionService;

  @InjectMocks
  private PendingSubscriptionController pendingSubscriptionController;

  private SubscriptionRequest req;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    req = SubscriptionRequest.builder()
        .approver(APPROVER_EMAIL)
        .email(TEST_EMAIL)
        .topic(TOPIC_ID)
        .build();

    when(animalInfoPendingSubscriptionService.getTopicId()).thenReturn(TOPIC_ID);
    when(pendingSubscriptionServiceStrategy.getServiceByTopic(TOPIC_ID))
        .thenReturn(Optional.of(animalInfoPendingSubscriptionService));
    when(pendingSubscriptionServiceStrategy.getAllServices())
        .thenReturn(List.of(animalInfoPendingSubscriptionService));
  }

  @Test
  void shouldReturnPendingSubscribers() {
    PendingSubscriber subscriber = PendingSubscriber.builder()
        .email(TEST_EMAIL)
        .topic(TOPIC_ID)
        .build();

    when(animalInfoPendingSubscriptionService.getSubscribersByApprover(APPROVER_EMAIL))
        .thenReturn(List.of(subscriber));

    List<PendingSubscriber> result = pendingSubscriptionController.getPendingSubscribers(req);

    assertNotNull(result);
    assertEquals(1, result.size());
    verify(animalInfoPendingSubscriptionService, times(1))
        .getSubscribersByApprover(APPROVER_EMAIL);
    assertEquals(subscriber, result.get(0));
  }

  @Test
  void shouldReturnPendingNoApproverSubscribers() {
    when(pendingSubscriptionServiceStrategy.getAllServices())
        .thenReturn(Collections.emptyList());

    List<PendingSubscriber> result = pendingSubscriptionController.getPendingNoApproverSubscribers();

    assertNotNull(result);
    assertEquals(0, result.size());
  }
}
