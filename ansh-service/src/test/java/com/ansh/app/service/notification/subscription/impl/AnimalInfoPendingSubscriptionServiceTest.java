package com.ansh.app.service.notification.subscription.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ansh.app.service.exception.user.UnauthorizedActionException;
import com.ansh.app.service.user.UserSubscriptionAuthorityService;
import com.ansh.app.service.user.impl.UserProfileServiceImpl;
import com.ansh.entity.account.UserProfile.AnimalInfoNotifStatus;
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

class AnimalInfoPendingSubscriptionServiceTest {

  private static final String ANIMAL_TOPIC = "animalTopicId";
  private static final String SUBSCRIBER_EMAIL = "subscriber@email.com";
  private static final String APPROVER_EMAIL = "approver@mail.com";
  @Mock
  private PendingSubscriptionDecisionProducer animalNotificationUserSubscribedProducer;

  @Mock
  private PendingSubscriberRepository pendingSubscriberRepository;

  @Mock
  private UserProfileServiceImpl userProfileService;

  @Mock
  private UserSubscriptionAuthorityService userSubscriptionAuthorityService;

  @InjectMocks
  private AnimalInfoPendingSubscriptionServiceImpl service;

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


    service = new AnimalInfoPendingSubscriptionServiceImpl(
        ANIMAL_TOPIC,
        pendingSubscriberRepository,
        userSubscriptionAuthorityService,
        animalNotificationUserSubscribedProducer,
        userProfileService
    );
  }

  @Test
  void shouldApproveSubscriber_whenExists() throws UnauthorizedActionException {
    doNothing().when(userSubscriptionAuthorityService).checkAuthorityToApprove(APPROVER_EMAIL);
    when(pendingSubscriberRepository.findByEmailAndTopic(SUBSCRIBER_EMAIL, ANIMAL_TOPIC))
        .thenReturn(Optional.of(pendingSubscriber));

    service.approveSubscriber(SUBSCRIBER_EMAIL, APPROVER_EMAIL);

    verify(pendingSubscriberRepository, times(1))
        .findByEmailAndTopic(SUBSCRIBER_EMAIL,
            ANIMAL_TOPIC);
    verify(animalNotificationUserSubscribedProducer, times(1))
        .sendApprove(SUBSCRIBER_EMAIL,
            APPROVER_EMAIL, ANIMAL_TOPIC);
    verify(pendingSubscriberRepository, times(1))
        .deleteByEmailAndTopic(SUBSCRIBER_EMAIL,
            ANIMAL_TOPIC);
  }

  @Test
  void shouldNotApproveSubscriber_whenHasNoAuthority() throws UnauthorizedActionException {
    doThrow(new UnauthorizedActionException("You do not have permission to approve requests."))
        .when(userSubscriptionAuthorityService)
        .checkAuthorityToApprove(APPROVER_EMAIL);

    assertThrows(UnauthorizedActionException.class, () -> {
      service.approveSubscriber(SUBSCRIBER_EMAIL, APPROVER_EMAIL);
    });
  }

  @Test
  void shouldNotApproveSubscriber_whenNotFound() throws UnauthorizedActionException {
    doNothing().when(userSubscriptionAuthorityService).checkAuthorityToApprove(APPROVER_EMAIL);
    doNothing().when(userSubscriptionAuthorityService).checkAuthorityToReject(APPROVER_EMAIL);
    when(pendingSubscriberRepository.findByEmailAndTopic(SUBSCRIBER_EMAIL, ANIMAL_TOPIC))
        .thenReturn(Optional.empty());

    service.approveSubscriber(SUBSCRIBER_EMAIL, APPROVER_EMAIL);

    verify(pendingSubscriberRepository, times(1))
        .findByEmailAndTopic(SUBSCRIBER_EMAIL,
            ANIMAL_TOPIC);
    verify(animalNotificationUserSubscribedProducer, never()).sendApprove(any(), any(), any());
    verify(pendingSubscriberRepository, never()).deleteByEmailAndTopic(any(), any());
  }

  @Test
  void shouldRejectSubscriber_whenExists() throws UnauthorizedActionException {
    doNothing().when(userSubscriptionAuthorityService).checkAuthorityToReject(APPROVER_EMAIL);
    when(pendingSubscriberRepository.findByEmailAndTopic(SUBSCRIBER_EMAIL, ANIMAL_TOPIC))
        .thenReturn(Optional.of(pendingSubscriber));

    service.rejectSubscriber(SUBSCRIBER_EMAIL, APPROVER_EMAIL);

    verify(pendingSubscriberRepository, times(1))
        .findByEmailAndTopic(SUBSCRIBER_EMAIL,
            ANIMAL_TOPIC);
    verify(animalNotificationUserSubscribedProducer, times(1))
        .sendReject(SUBSCRIBER_EMAIL,
            APPROVER_EMAIL, ANIMAL_TOPIC);
    verify(pendingSubscriberRepository, times(1))
        .deleteByEmailAndTopic(SUBSCRIBER_EMAIL,
            ANIMAL_TOPIC);
  }

  @Test
  void shouldNotRejectSubscriber_whenHasNoAutority() throws UnauthorizedActionException {
    doThrow(new UnauthorizedActionException("You do not have permission to reject requests."))
        .when(userSubscriptionAuthorityService)
        .checkAuthorityToReject(APPROVER_EMAIL);

    assertThrows(UnauthorizedActionException.class, () -> {
      service.rejectSubscriber(SUBSCRIBER_EMAIL, APPROVER_EMAIL);
    });
  }

  @Test
  void shouldNotRejectSubscriber_whenNotFound() throws UnauthorizedActionException {
    doNothing().when(userSubscriptionAuthorityService).checkAuthorityToReject(APPROVER_EMAIL);
    when(pendingSubscriberRepository.findByEmailAndTopic(SUBSCRIBER_EMAIL, ANIMAL_TOPIC))
        .thenReturn(Optional.empty());

    service.rejectSubscriber(SUBSCRIBER_EMAIL, APPROVER_EMAIL);

    verify(pendingSubscriberRepository, times(1))
        .findByEmailAndTopic(SUBSCRIBER_EMAIL,
            ANIMAL_TOPIC);
    verify(animalNotificationUserSubscribedProducer, never()).sendReject(any(), any(), any());
    verify(pendingSubscriberRepository, never()).deleteByEmailAndTopic(any(), any());
  }

  @Test
  void shouldSavePendingSubscriber() {

    when(pendingSubscriberRepository.findByEmailAndTopic(SUBSCRIBER_EMAIL, ANIMAL_TOPIC))
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

    when(pendingSubscriberRepository.findByEmailAndTopic(email, ANIMAL_TOPIC))
        .thenReturn(Optional.of(subscriber1));

    service.saveSubscriber(email, approver);

    verify(pendingSubscriberRepository, times(0)).save(any());

    verify(userProfileService, times(0)).updateAnimalNotificationSubscriptionStatus(
        email, AnimalInfoNotifStatus.PENDING);
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

    when(pendingSubscriberRepository.findByApproverAndTopic(approver, ANIMAL_TOPIC)
    ).thenReturn(mockSubscribers);

    List<PendingSubscriber> result = service.getSubscribersByApprover(approver);

    assertEquals(2, result.size());
    verify(pendingSubscriberRepository).findByApproverAndTopic(approver, ANIMAL_TOPIC);
  }
}
