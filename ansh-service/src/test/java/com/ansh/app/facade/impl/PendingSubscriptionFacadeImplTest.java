package com.ansh.app.facade.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ansh.app.controller.PendingSubscriptionController;
import com.ansh.app.service.notification.subscription.PendingSubscriptionService;
import com.ansh.dto.SubscriptionRequest;
import com.ansh.notification.strategy.PendingSubscriptionServiceStrategy;
import com.ansh.repository.entity.PendingSubscriber;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class PendingSubscriptionFacadeImplTest {

  private static final String APPROVER_EMAIL = "admin@example.com";
  private static final String TEST_EMAIL = "test@example.com";
  private static final String TOPIC_ID = "animalTopicId";

  @Mock
  private PendingSubscriptionServiceStrategy pendingSubscriptionServiceStrategy;

  @Mock
  private PendingSubscriptionService animalInfoPendingSubscriptionService;

  @InjectMocks
  private PendingSubscriptionFacadeImpl pendingSubscriptionFacade;


  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

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

    List<PendingSubscriber> result = pendingSubscriptionFacade.getSubscribersByApprover(APPROVER_EMAIL);

    assertNotNull(result);
    assertEquals(1, result.size());
    verify(animalInfoPendingSubscriptionService, times(1))
        .getSubscribersByApprover(APPROVER_EMAIL);
    assertEquals(subscriber, result.getFirst());
  }

  @Test
  void shouldReturnPendingNoApproverSubscribers() {
    when(pendingSubscriptionServiceStrategy.getAllServices())
        .thenReturn(Collections.emptyList());

    List<PendingSubscriber> result = pendingSubscriptionFacade.getPendingSubscribersWithoutApprover();

    assertNotNull(result);
    assertEquals(0, result.size());
  }
}
