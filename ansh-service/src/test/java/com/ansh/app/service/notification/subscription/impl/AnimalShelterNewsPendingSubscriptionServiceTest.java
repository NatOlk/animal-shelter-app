package com.ansh.app.service.notification.subscription.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ansh.app.service.user.UserProfileService;
import com.ansh.entity.account.UserProfile.AnimalInfoNotifStatus;
import com.ansh.event.AnimalShelterTopic;
import com.ansh.notification.subscription.PendingSubscriptionDecisionProducer;
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

class AnimalShelterNewsPendingSubscriptionServiceTest {

  private static final String ANIMAL_TOPIC = AnimalShelterTopic.ANIMAL_SHELTER_NEWS.getTopicName();
  private static final String SUBSCRIBER_EMAIL = "subscriber@email.com";
  private static final String APPROVER_EMAIL = "approver@mail.com";
  @Mock
  private PendingSubscriptionDecisionProducer animalNotificationUserSubscribedProducer;

  @Mock
  private PendingSubscriberRepository pendingSubscriberRepository;

  @Mock
  private UserProfileService userProfileService;

  @InjectMocks
  private AnimalShelterNewsPendingSubscriptionServiceImpl service;

  @Captor
  private ArgumentCaptor<PendingSubscriber> subscriberCaptor;

  private PendingSubscriber pendingSubscriber;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    pendingSubscriber = new PendingSubscriber();
    pendingSubscriber.setEmail(SUBSCRIBER_EMAIL);
    pendingSubscriber.setTopic(ANIMAL_TOPIC);
    pendingSubscriber.setApprover(APPROVER_EMAIL);

    service = new AnimalShelterNewsPendingSubscriptionServiceImpl(
        pendingSubscriberRepository,
        animalNotificationUserSubscribedProducer,
        userProfileService
    );
  }

  @Test
  void shouldApproveSubscriber_whenExists() {
    when(pendingSubscriberRepository.findPendingByEmailAndTopic(SUBSCRIBER_EMAIL, ANIMAL_TOPIC))
        .thenReturn(Optional.of(pendingSubscriber));

    service.approveSubscriber(SUBSCRIBER_EMAIL, APPROVER_EMAIL);

    verify(pendingSubscriberRepository, times(1))
        .findPendingByEmailAndTopic(SUBSCRIBER_EMAIL,
            ANIMAL_TOPIC);
    verify(animalNotificationUserSubscribedProducer, times(1))
        .sendApprove(SUBSCRIBER_EMAIL,
            APPROVER_EMAIL, ANIMAL_TOPIC);
    verify(pendingSubscriberRepository, times(1))
        .updateApprovalStatus(SUBSCRIBER_EMAIL, ANIMAL_TOPIC, true);
  }

  @Test
  void shouldNotApproveSubscriber_whenNotFound() {
    when(pendingSubscriberRepository.findPendingByEmailAndTopic(SUBSCRIBER_EMAIL, ANIMAL_TOPIC))
        .thenReturn(Optional.empty());

    service.approveSubscriber(SUBSCRIBER_EMAIL, APPROVER_EMAIL);

    verify(pendingSubscriberRepository, times(1))
        .findPendingByEmailAndTopic(SUBSCRIBER_EMAIL,
            ANIMAL_TOPIC);
    verify(animalNotificationUserSubscribedProducer, never()).sendApprove(any(), any(), any());
    verify(pendingSubscriberRepository, never()).updateApprovalStatus(any(), any(), eq(true));
  }

  @Test
  void shouldRejectSubscriber_whenExists() {
    when(pendingSubscriberRepository.findPendingByEmailAndTopic(SUBSCRIBER_EMAIL, ANIMAL_TOPIC))
        .thenReturn(Optional.of(pendingSubscriber));

    service.rejectSubscriber(SUBSCRIBER_EMAIL, APPROVER_EMAIL);

    verify(pendingSubscriberRepository, times(1))
        .findPendingByEmailAndTopic(SUBSCRIBER_EMAIL, ANIMAL_TOPIC);
    verify(animalNotificationUserSubscribedProducer, times(1))
        .sendReject(SUBSCRIBER_EMAIL, APPROVER_EMAIL, ANIMAL_TOPIC);
    verify(pendingSubscriberRepository, times(1))
        .updateApprovalStatus(SUBSCRIBER_EMAIL, ANIMAL_TOPIC, false);
  }


  @Test
  void shouldNotRejectSubscriber_whenNotFound() {
    when(pendingSubscriberRepository.findPendingByEmailAndTopic(SUBSCRIBER_EMAIL, ANIMAL_TOPIC))
        .thenReturn(Optional.empty());

    service.rejectSubscriber(SUBSCRIBER_EMAIL, APPROVER_EMAIL);

    verify(pendingSubscriberRepository, times(1))
        .findPendingByEmailAndTopic(SUBSCRIBER_EMAIL, ANIMAL_TOPIC);
    verify(animalNotificationUserSubscribedProducer, never()).sendReject(any(), any(), any());
    verify(pendingSubscriberRepository, never()).updateApprovalStatus(any(), any(), eq(false));
  }

  @Test
  void shouldSavePendingSubscriber() {

    when(pendingSubscriberRepository.findPendingByEmailAndTopic(SUBSCRIBER_EMAIL, ANIMAL_TOPIC))
        .thenReturn(Optional.empty());

    service.saveSubscriber(SUBSCRIBER_EMAIL, APPROVER_EMAIL);

    verify(pendingSubscriberRepository).save(subscriberCaptor.capture());
    PendingSubscriber capturedSubscriber = subscriberCaptor.getValue();
    assertEquals(SUBSCRIBER_EMAIL, capturedSubscriber.getEmail());
    assertEquals(APPROVER_EMAIL, capturedSubscriber.getApprover());
    assertEquals(ANIMAL_TOPIC, capturedSubscriber.getTopic());

    verify(userProfileService).updateAnimalNotificationSubscriptionStatus(
        SUBSCRIBER_EMAIL, AnimalInfoNotifStatus.PENDING);
  }

  @Test
  void shouldNotSavePendingSubscriber_whenSibscriberExist() {

    String email = SUBSCRIBER_EMAIL;
    String approver = APPROVER_EMAIL;

    PendingSubscriber subscriber1 = new PendingSubscriber();
    subscriber1.setEmail(email);
    subscriber1.setApprover(approver);
    subscriber1.setTopic(ANIMAL_TOPIC);

    when(pendingSubscriberRepository.findPendingByEmailAndTopic(email, ANIMAL_TOPIC))
        .thenReturn(Optional.of(subscriber1));

    service.saveSubscriber(email, approver);

    verify(pendingSubscriberRepository, times(0)).save(any());

    verify(userProfileService, times(0))
        .updateAnimalNotificationSubscriptionStatus(email, AnimalInfoNotifStatus.PENDING);
  }

  @Test
  void shouldReturnPendingSubscribers() {
    String approver = APPROVER_EMAIL;
    PendingSubscriber subscriber1 = new PendingSubscriber();
    subscriber1.setEmail("email1@example.com");
    subscriber1.setApprover(approver);
    subscriber1.setTopic("topic1");

    PendingSubscriber subscriber2 = new PendingSubscriber();
    subscriber2.setEmail("email2@example.com");
    subscriber2.setApprover(approver);
    subscriber2.setTopic("topic2");

    List<PendingSubscriber> mockSubscribers = List.of(subscriber1, subscriber2);

    when(pendingSubscriberRepository.findPendingByApproverAndTopic(approver, ANIMAL_TOPIC))
        .thenReturn(mockSubscribers);

    List<PendingSubscriber> result = service.getSubscribersByApprover(approver);

    assertEquals(2, result.size());
    verify(pendingSubscriberRepository).findPendingByApproverAndTopic(approver, ANIMAL_TOPIC);
  }
}
