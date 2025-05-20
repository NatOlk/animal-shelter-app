package com.ansh.app.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ansh.app.facade.PendingSubscriptionFacade;
import com.ansh.dto.SubscriptionRequest;
import com.ansh.repository.entity.PendingSubscriber;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class PendingSubscriptionControllerTest {

  private static final String APPROVER_EMAIL = "admin@example.com";
  private static final String TEST_EMAIL = "test@example.com";
  private static final String TOPIC_ID = "animalTopicId";

  @Mock
  private PendingSubscriptionFacade pendingSubscriptionFacade;

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
  }

  @Test
  void shouldReturnPendingSubscribers() {
    // given
    PendingSubscriber subscriber = PendingSubscriber.builder()
        .email(TEST_EMAIL)
        .topic(TOPIC_ID)
        .build();

    when(pendingSubscriptionFacade.getSubscribersByApprover(APPROVER_EMAIL))
        .thenReturn(List.of(subscriber));

    // when
    List<PendingSubscriber> result = pendingSubscriptionController.getPendingSubscribers(req);

    // then
    assertNotNull(result);
    assertEquals(1, result.size());
    verify(pendingSubscriptionFacade, times(1))
        .getSubscribersByApprover(APPROVER_EMAIL);
    assertEquals(subscriber, result.getFirst());
  }

  @Test
  void shouldReturnPendingNoApproverSubscribers() {
    // given
    PendingSubscriber subscriber = PendingSubscriber.builder()
        .email(TEST_EMAIL)
        .topic(TOPIC_ID)
        .build();
    when(pendingSubscriptionFacade.getPendingSubscribersWithoutApprover())
        .thenReturn(List.of(subscriber));
    // when
    List<PendingSubscriber> result = pendingSubscriptionController.getPendingNoApproverSubscribers();

    // then
    assertNotNull(result);
    assertEquals(1, result.size());
    verify(pendingSubscriptionFacade, times(1))
        .getPendingSubscribersWithoutApprover();
    assertEquals(subscriber, result.getFirst());
  }
}
