package com.ansh.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ansh.entity.subscription.Subscription;
import com.ansh.notification.subscription.SubscriberNotificationEventProducer;
import com.ansh.repository.SubscriptionRepository;
import com.ansh.service.SubscriptionNotificationEmailService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;


class AnimalTopicSubscriberRegistryServiceTest {

  private static final String ANIMAL_TOPIC = "animalTopicId";

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
  private AnimalTopicSubscriberRegistryServiceImpl registryService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    registryService.setAnimalTopicId(ANIMAL_TOPIC);
  }

  @Test
  void testRegisterSubscriber_whenSubscriptionDoesNotExist() {
    String email = "test@example.com";
    String approver = "approver@example.com";

    when(subscriptionRepository.findByEmailAndTopic(email, ANIMAL_TOPIC))
        .thenReturn(Optional.empty());

    registryService.registerSubscriber(email, approver);

    verify(subscriptionRepository, times(1)).save(any(Subscription.class));
    verify(subscriberNotificationInfoProducer, times(1))
        .sendApproveRequest(email, approver, ANIMAL_TOPIC);
  }

  @Test
  void testRegisterSubscriber_whenSubscriptionExists() {
    String email = "test@example.com";
    Subscription existingSubscription = new Subscription();
    existingSubscription.setApproved(true);
    existingSubscription.setAccepted(true);

    when(subscriptionRepository.findByEmailAndTopic(email, ANIMAL_TOPIC))
        .thenReturn(Optional.of(existingSubscription));

    registryService.registerSubscriber(email, "approver@example.com");

    verify(subscriptionRepository, never()).save(any(Subscription.class));
    verify(subscriptionNotificationService, times(1))
        .sendRepeatConfirmationEmail(existingSubscription);
  }
}

