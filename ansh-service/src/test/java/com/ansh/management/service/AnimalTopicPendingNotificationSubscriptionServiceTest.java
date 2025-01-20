package com.ansh.management.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ansh.auth.service.UserProfileService;
import com.ansh.entity.animal.UserProfile;
import com.ansh.notification.AnimalNotificationUserSubscribedProducer;
import com.ansh.repository.PendingSubscriberRepository;
import com.ansh.repository.entity.PendingSubscriber;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class AnimalTopicPendingNotificationSubscriptionServiceTest {

  private static final String ANIMAL_TOPIC = "animalTopic";
  @Mock
  private AnimalNotificationUserSubscribedProducer animalNotificationUserSubscribedProducer;

  @Mock
  private PendingSubscriberRepository pendingSubscriberRepository;

  @Mock
  private UserProfileService userProfileService;

  @InjectMocks
  private AnimalTopicPendingSubscriptionService animalTopicPendingSubscriptionService;

  @Captor
  private ArgumentCaptor<PendingSubscriber> subscriberCaptor;

  private PendingSubscriber pendingSubscriber;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    pendingSubscriber = new PendingSubscriber();
    pendingSubscriber.setEmail("test@example.com");
    pendingSubscriber.setTopic("animalTopic");
    pendingSubscriber.setApprover("approver@example.com");
    animalTopicPendingSubscriptionService.setAnimalTopicId(ANIMAL_TOPIC);
  }

  @Test
  void shouldApproveSubscriber_whenExists() {
    when(pendingSubscriberRepository.findByEmailAndTopic("test@example.com", "animalTopic"))
        .thenReturn(Optional.of(pendingSubscriber));

    animalTopicPendingSubscriptionService.approveSubscriber("test@example.com", "approver@example.com");

    verify(pendingSubscriberRepository, times(1))
        .findByEmailAndTopic("test@example.com",
            "animalTopic");
    verify(animalNotificationUserSubscribedProducer, times(1))
        .sendApprove("test@example.com",
            "approver@example.com", "animalTopic");
    verify(pendingSubscriberRepository, times(1))
        .deleteByEmailAndTopic("test@example.com",
            "animalTopic");
  }

  @Test
  void shouldNotApproveSubscriber_whenNotFound() {
    when(pendingSubscriberRepository.findByEmailAndTopic("test@example.com", "animalTopic"))
        .thenReturn(Optional.empty());

    animalTopicPendingSubscriptionService.approveSubscriber("test@example.com", "approver@example.com");

    verify(pendingSubscriberRepository, times(1))
        .findByEmailAndTopic("test@example.com",
            "animalTopic");
    verify(animalNotificationUserSubscribedProducer, never()).sendApprove(any(), any(), any());
    verify(pendingSubscriberRepository, never()).deleteByEmailAndTopic(any(), any());
  }

  @Test
  void shouldRejectSubscriber_whenExists() {
    when(pendingSubscriberRepository.findByEmailAndTopic("test@example.com", "animalTopic"))
        .thenReturn(Optional.of(pendingSubscriber));

    animalTopicPendingSubscriptionService.rejectSubscriber("test@example.com");

    verify(pendingSubscriberRepository, times(1))
        .findByEmailAndTopic("test@example.com",
            "animalTopic");
    verify(animalNotificationUserSubscribedProducer, times(1))
        .sendReject("test@example.com",
            "approver@example.com", "animalTopic");
    verify(pendingSubscriberRepository, times(1))
        .deleteByEmailAndTopic("test@example.com",
            "animalTopic");
  }

  @Test
  void shouldNotRejectSubscriber_whenNotFound() {
    when(pendingSubscriberRepository.findByEmailAndTopic("test@example.com", "animalTopic"))
        .thenReturn(Optional.empty());

    animalTopicPendingSubscriptionService.rejectSubscriber("test@example.com");

    verify(pendingSubscriberRepository, times(1))
        .findByEmailAndTopic("test@example.com",
            "animalTopic");
    verify(animalNotificationUserSubscribedProducer, never()).sendReject(any(), any(), any());
    verify(pendingSubscriberRepository, never()).deleteByEmailAndTopic(any(), any());
  }

  @Test
  void shouldSavePendingSubscriber() {
    String email = "test@example.com";
    String approver = "approver@example.com";

    when(pendingSubscriberRepository.findByEmailAndTopic(email, ANIMAL_TOPIC))
        .thenReturn(Optional.empty());

    animalTopicPendingSubscriptionService.saveSubscriber(email, approver);

    verify(pendingSubscriberRepository).save(subscriberCaptor.capture());
    PendingSubscriber capturedSubscriber = subscriberCaptor.getValue();
    assertEquals(email, capturedSubscriber.getEmail());
    assertEquals(approver, capturedSubscriber.getApprover());
    assertEquals(ANIMAL_TOPIC, capturedSubscriber.getTopic());

    verify(userProfileService).updateAnimalNotificationSubscriptionStatus(
        email, UserProfile.AnimalNotificationSubscriptionStatus.PENDING);
  }

  @Test
  void shouldNotSavePendingSubscriber_whenSibscriberExist() {
    String email = "test@example.com";
    String approver = "approver@example.com";

    PendingSubscriber subscriber1 = new PendingSubscriber();
    subscriber1.setEmail(email);
    subscriber1.setApprover(approver);
    subscriber1.setTopic(ANIMAL_TOPIC);

    when(pendingSubscriberRepository.findByEmailAndTopic(email, ANIMAL_TOPIC))
        .thenReturn(Optional.of(subscriber1));

    animalTopicPendingSubscriptionService.saveSubscriber(email, approver);

    verify(pendingSubscriberRepository, times(0)).save(any());

    verify(userProfileService, times(0)).updateAnimalNotificationSubscriptionStatus(
        email, UserProfile.AnimalNotificationSubscriptionStatus.PENDING);
  }

  @Test
  void shouldReturnPendingSubscribers() {
    String approver = "approver@example.com";
    PendingSubscriber subscriber1 = new PendingSubscriber();
    subscriber1.setEmail("email1@example.com");
    subscriber1.setApprover(approver);
    subscriber1.setTopic("topic1");

    PendingSubscriber subscriber2 = new PendingSubscriber();
    subscriber2.setEmail("email2@example.com");
    subscriber2.setApprover(approver);
    subscriber2.setTopic("topic2");

    List<PendingSubscriber> mockSubscribers = List.of(subscriber1, subscriber2);

    when(pendingSubscriberRepository.findByApproverAndTopic(approver, ANIMAL_TOPIC)
    ).thenReturn(mockSubscribers);

    List<PendingSubscriber> result = animalTopicPendingSubscriptionService.getSubscribersByApprover(approver);

    assertEquals(2, result.size());
    verify(pendingSubscriberRepository).findByApproverAndTopic(approver, ANIMAL_TOPIC);
  }
}
