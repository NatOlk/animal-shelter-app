package com.ansh.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.ansh.entity.subscription.Subscription;
import com.ansh.event.AnimalShelterTopic;
import com.ansh.notification.subscription.SubscriberNotificationEventProducer;
import com.ansh.repository.SubscriptionRepository;
import com.ansh.service.SubscriptionNotificationEmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Optional;

class AnimalShelterNewsSubscriberRegistryServiceTest {

  private static final String ANIMAL_TOPIC = AnimalShelterTopic.ANIMAL_SHELTER_NEWS.getTopicName();
  private static final String TEST_EMAIL = "test@test.com";
  private static final String APPROVER_EMAIL = "approver@test.com";

  @Mock
  private SubscriptionRepository subscriptionRepository;

  @Mock
  private SubscriberNotificationEventProducer subscriberNotificationInfoProducer;

  @Mock
  private SubscriptionNotificationEmailService subscriptionNotificationService;

  @Mock
  private RedisTemplate<String, Subscription> subscriptionRedisTemplate;

  @Mock
  private RedisTemplate<String, String> updRedisTemplate;

  @InjectMocks
  private AnimalShelterNewsTopicSubscriberRegistryServiceImpl registryService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testRegisterSubscriber_whenSubscriptionDoesNotExist() {
    // given
    when(subscriptionRepository.findByEmailAndTopic(TEST_EMAIL, ANIMAL_TOPIC))
        .thenReturn(Optional.empty());

    // when
    registryService.registerSubscriber(TEST_EMAIL, APPROVER_EMAIL);

    // then
    verify(subscriptionRepository, times(1)).save(any(Subscription.class));
    verify(subscriberNotificationInfoProducer, times(1))
        .sendPendingApproveRequest(TEST_EMAIL, APPROVER_EMAIL, ANIMAL_TOPIC);
    verifyNoInteractions(subscriptionNotificationService);
  }

  @Test
  void testRegisterSubscriber_whenSubscriptionExists() {
    // given
    Subscription existingSubscription = Subscription.builder()
        .email(TEST_EMAIL)
        .topic(ANIMAL_TOPIC)
        .accepted(true)
        .approved(true)
        .build();

    when(subscriptionRepository.findByEmailAndTopic(TEST_EMAIL, ANIMAL_TOPIC))
        .thenReturn(Optional.of(existingSubscription));

    // when
    registryService.registerSubscriber(TEST_EMAIL, APPROVER_EMAIL);

    // then
    verify(subscriptionRepository, never()).save(any(Subscription.class));
    verify(subscriptionNotificationService, times(1))
        .sendRepeatConfirmationEmail(existingSubscription);
    verifyNoInteractions(subscriberNotificationInfoProducer);
  }
}