package com.ansh.service.impl;

import static com.ansh.entity.account.UserProfile.AnimalInfoNotifStatus.ACTIVE;
import static com.ansh.entity.account.UserProfile.AnimalInfoNotifStatus.NONE;
import static com.ansh.entity.account.UserProfile.AnimalInfoNotifStatus.PENDING;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.ansh.cache.SubscriptionCacheManager;
import com.ansh.entity.subscription.Subscription;
import com.ansh.event.AnimalShelterTopic;
import com.ansh.notification.subscription.SubscriberNotificationEventProducer;
import com.ansh.repository.SubscriptionRepository;
import com.ansh.service.SubscriptionNotificationEmailService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class VaccinationTopicSubscriberRegistryServiceTest {

  private static final String VACCINATION_TOPIC = AnimalShelterTopic.VACCINATION_INFO.getTopicName();
  private static final String TEST_EMAIL = "test@test.com";
  private static final String APPROVER_EMAIL = "approver@test.com";

  @Mock
  private SubscriptionRepository subscriptionRepository;
  @Mock
  private SubscriberNotificationEventProducer subscriberNotificationInfoProducer;
  @Mock
  private SubscriptionNotificationEmailService subscriptionNotificationService;
  @Mock
  private SubscriptionCacheManager cacheManager;
  @InjectMocks
  private VaccinationTopicSubscriberRegistryServiceImpl registryService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testRegisterSubscriber_whenSubscriptionDoesNotExist() {
    // given
    when(subscriptionRepository.findByEmailAndTopic(TEST_EMAIL, VACCINATION_TOPIC))
        .thenReturn(Optional.empty());

    // when
    registryService.registerSubscriber(TEST_EMAIL, APPROVER_EMAIL);

    // then
    verify(subscriptionRepository, times(1)).save(any(Subscription.class));
    verify(subscriberNotificationInfoProducer, times(1))
        .sendPendingApproveRequest(TEST_EMAIL, APPROVER_EMAIL, VACCINATION_TOPIC);
    // check that no email is sent
    verifyNoInteractions(subscriptionNotificationService);
  }

  @Test
  void testRegisterSubscriber_whenSubscriptionExists() {
    // given
    Subscription existingSubscription = Subscription.builder()
        .email(TEST_EMAIL)
        .topic(VACCINATION_TOPIC)
        .accepted(true)
        .approved(true)
        .build();

    when(subscriptionRepository.findByEmailAndTopic(TEST_EMAIL, VACCINATION_TOPIC))
        .thenReturn(Optional.of(existingSubscription));

    // when
    registryService.registerSubscriber(TEST_EMAIL, APPROVER_EMAIL);

    // then
    verify(subscriptionRepository, never()).save(any(Subscription.class));
    verify(subscriptionNotificationService, never())
        .sendRepeatConfirmationEmail(existingSubscription);
    verifyNoInteractions(subscriberNotificationInfoProducer);
  }

  @Test
  void testAcceptSubscription_whenTokenExists() {
    // given
    Subscription subscription = Subscription.builder()
        .token("testToken")
        .email(TEST_EMAIL)
        .topic(VACCINATION_TOPIC)
        .build();

    when(subscriptionRepository.findByTokenAndTopic("testToken", VACCINATION_TOPIC))
        .thenReturn(Optional.of(subscription));

    boolean result = registryService.acceptSubscription("testToken");

    assertTrue(result);
    verify(subscriptionRepository).save(subscription);
  }

  @Test
  void testAcceptSubscription_whenTokenDoesNotExist() {
    // given
    when(subscriptionRepository.findByTokenAndTopic("invalidToken", VACCINATION_TOPIC)).thenReturn(
        Optional.empty());

    boolean result = registryService.acceptSubscription("invalidToken");

    assertFalse(result);
  }

  @Test
  void testUnsubscribe_byToken() {
    registryService.unsubscribe("tokenToRemove");

    verify(subscriptionRepository).deleteByTokenAndTopic("tokenToRemove", VACCINATION_TOPIC);
    verify(cacheManager).removeFromCache("tokenToRemove");
  }

  @Test
  void testUnsubscribe_byEmailAndApprover() {
    // given
    Subscription subscription = Subscription.builder()
        .token("tokenToRemove")
        .email(TEST_EMAIL)
        .topic(VACCINATION_TOPIC)
        .build();

    when(subscriptionRepository.findByEmailAndTopic(TEST_EMAIL, VACCINATION_TOPIC))
        .thenReturn(Optional.of(subscription));

    registryService.unsubscribe(TEST_EMAIL, APPROVER_EMAIL);

    verify(subscriptionRepository).deleteByEmailAndTopic(TEST_EMAIL, VACCINATION_TOPIC);
    verify(cacheManager).removeFromCache(subscription.getToken());
  }

  @Test
  void testGetSubscriptionStatus_none() {
    // given
    when(subscriptionRepository.findByEmailAndTopic(TEST_EMAIL, VACCINATION_TOPIC)).thenReturn(
        Optional.empty());

    var status = registryService.getSubscriptionStatus(TEST_EMAIL);

    assertEquals(NONE, status);
  }

  @Test
  void testGetSubscriptionStatus_pending() {
    // given
    Subscription subscription = Subscription.builder()
        .email(TEST_EMAIL)
        .topic(VACCINATION_TOPIC)
        .accepted(false)
        .build();

    when(subscriptionRepository.findByEmailAndTopic(TEST_EMAIL, VACCINATION_TOPIC)).thenReturn(
        Optional.of(subscription));

    var status = registryService.getSubscriptionStatus(TEST_EMAIL);

    assertEquals(PENDING, status);
  }

  @Test
  void testGetSubscriptionStatus_active() {
    // given
    Subscription subscription = Subscription.builder()
        .email(TEST_EMAIL)
        .topic(VACCINATION_TOPIC)
        .accepted(true)
        .build();

    when(subscriptionRepository.findByEmailAndTopic(TEST_EMAIL, VACCINATION_TOPIC)).thenReturn(
        Optional.of(subscription));

    var status = registryService.getSubscriptionStatus(TEST_EMAIL);

    assertEquals(ACTIVE, status);
  }

  @Test
  void testHandleSubscriptionApproval_reject() {
    // given
    Subscription subscription = Subscription.builder()
        .token("rejectToken")
        .email(TEST_EMAIL)
        .topic(VACCINATION_TOPIC)
        .build();

    when(subscriptionRepository.findByEmailAndTopic(TEST_EMAIL, VACCINATION_TOPIC)).thenReturn(
        Optional.of(subscription));

    registryService.handleSubscriptionApproval(TEST_EMAIL, APPROVER_EMAIL, true);

    verify(subscriptionRepository).deleteByTokenAndTopic("rejectToken", VACCINATION_TOPIC);
    verify(cacheManager).removeFromCache("rejectToken");
  }

  @Test
  void testHandleSubscriptionApproval_approveWithAutoAccept() {
    // given
    Subscription subscription = Subscription.builder()
        .token("approveToken")
        .email(TEST_EMAIL)
        .topic(VACCINATION_TOPIC)
        .build();

    when(subscriptionRepository.findByEmailAndTopic(TEST_EMAIL, VACCINATION_TOPIC)).thenReturn(
        Optional.of(subscription));

    registryService.handleSubscriptionApproval(TEST_EMAIL, APPROVER_EMAIL, false);

    verify(subscriptionRepository, times(2)).save(subscription);
    verify(cacheManager, times(1)).addToCache(subscription);
    // check that no email is sent
    verifyNoInteractions(subscriptionNotificationService);
  }
}
