package com.ansh.uimanagement.service;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ansh.notification.AnimalNotificationUserSubscribedProducer;
import com.ansh.repository.PendingSubscriberRepository;
import com.ansh.repository.entity.PendingSubscriber;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class AnimalTopicSubscriptionServiceTest {

  @Mock
  private AnimalNotificationUserSubscribedProducer animalNotificationUserSubscribedProducer;

  @Mock
  private PendingSubscriberRepository pendingSubscriberRepository;

  @InjectMocks
  private AnimalTopicSubscriptionService animalTopicSubscriptionService;

  private PendingSubscriber pendingSubscriber;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    pendingSubscriber = new PendingSubscriber();
    pendingSubscriber.setEmail("test@example.com");
    pendingSubscriber.setTopic("animalTopic");
    pendingSubscriber.setApprover("approver@example.com");
    animalTopicSubscriptionService.setAnimalTopicId("animalTopic");
  }

  @Test
  void shouldApproveSubscriber_whenExists() {
    when(pendingSubscriberRepository.findByEmailAndTopic("test@example.com", "animalTopic"))
        .thenReturn(Optional.of(pendingSubscriber));

    animalTopicSubscriptionService.approveSubscriber("test@example.com", "approver@example.com");

    verify(pendingSubscriberRepository, times(1)).findByEmailAndTopic("test@example.com",
        "animalTopic");
    verify(animalNotificationUserSubscribedProducer, times(1)).sendApprove("test@example.com",
        "approver@example.com", "animalTopic");
    verify(pendingSubscriberRepository, times(1)).deleteByEmailAndTopic("test@example.com",
        "animalTopic");
  }

  @Test
  void shouldNotApproveSubscriber_whenNotFound() {
    when(pendingSubscriberRepository.findByEmailAndTopic("test@example.com", "animalTopic"))
        .thenReturn(Optional.empty());

    animalTopicSubscriptionService.approveSubscriber("test@example.com", "approver@example.com");

    verify(pendingSubscriberRepository, times(1)).findByEmailAndTopic("test@example.com",
        "animalTopic");
    verify(animalNotificationUserSubscribedProducer, never()).sendApprove(any(), any(), any());
    verify(pendingSubscriberRepository, never()).deleteByEmailAndTopic(any(), any());
  }

  @Test
  void shouldRejectSubscriber_whenExists() {
    when(pendingSubscriberRepository.findByEmailAndTopic("test@example.com", "animalTopic"))
        .thenReturn(Optional.of(pendingSubscriber));

    animalTopicSubscriptionService.rejectSubscriber("test@example.com");

    verify(pendingSubscriberRepository, times(1)).findByEmailAndTopic("test@example.com",
        "animalTopic");
    verify(animalNotificationUserSubscribedProducer, times(1)).sendReject("test@example.com",
        "approver@example.com", "animalTopic");
    verify(pendingSubscriberRepository, times(1)).deleteByEmailAndTopic("test@example.com",
        "animalTopic");
  }

  @Test
  void shouldNotRejectSubscriber_whenNotFound() {
    when(pendingSubscriberRepository.findByEmailAndTopic("test@example.com", "animalTopic"))
        .thenReturn(Optional.empty());

    animalTopicSubscriptionService.rejectSubscriber("test@example.com");

    verify(pendingSubscriberRepository, times(1)).findByEmailAndTopic("test@example.com",
        "animalTopic");
    verify(animalNotificationUserSubscribedProducer, never()).sendReject(any(), any(), any());
    verify(pendingSubscriberRepository, never()).deleteByEmailAndTopic(any(), any());
  }
}
