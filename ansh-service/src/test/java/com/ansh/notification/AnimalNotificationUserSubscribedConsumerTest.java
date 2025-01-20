package com.ansh.notification;

import static org.mockito.Mockito.*;

import com.ansh.event.subscription.AnimalNotificationUserSubscribedEvent;
import com.ansh.management.service.AnimalTopicPendingSubscriptionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class AnimalNotificationUserSubscribedConsumerTest {

  private static final String SUBSCRIPTION_TOPIC = "subscriptionTopic";

  private static final String ANIMAL_TOPIC = "animalTopic";

  @Mock
  private AnimalTopicPendingSubscriptionService animalTopicPendingSubscriptionService;

  @InjectMocks
  private AnimalNotificationUserSubscribedConsumer consumer;

  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    consumer.setSubscriptionTopicId(SUBSCRIPTION_TOPIC);
    consumer.setAnimalTopicId(ANIMAL_TOPIC);
    objectMapper = new ObjectMapper();
  }

  @Test
  void listen_validMessage_shouldCallSavePendingSubscriber() throws Exception {
    String json = "{\"email\":\"test@example.com\", \"approver\":\"admin\", \"topic\":\"animalTopic\"}";
    ConsumerRecord<String, String> message = new ConsumerRecord<>(SUBSCRIPTION_TOPIC, 0, 0L, "key", json);

    AnimalNotificationUserSubscribedEvent event = objectMapper.readValue(json, AnimalNotificationUserSubscribedEvent.class);

    consumer.listen(message);

    verify(animalTopicPendingSubscriptionService, times(1))
        .saveSubscriber(event.getEmail(), event.getApprover());
  }

  @Test
  void listen_invalidMessage_shouldNotCallSavePendingSubscriber() {
    String invalidJSon = "{\"invalidField\":\"value\"}";
    ConsumerRecord<String, String> message = new ConsumerRecord<>(SUBSCRIPTION_TOPIC, 0, 0L, "key", invalidJSon);

    consumer.listen(message);

    verify(animalTopicPendingSubscriptionService, never()).saveSubscriber(anyString(), anyString());
  }

  @Test
  void listen_invalidTopic_shouldCallSavePendingSubscriber() throws Exception {
    String json = "{\"email\":\"test@example.com\", \"approver\":\"admin\", \"topic\":\"notanimalTopic\"}";
    ConsumerRecord<String, String> message = new ConsumerRecord<>(SUBSCRIPTION_TOPIC, 0, 0L, "key", json);

    AnimalNotificationUserSubscribedEvent event = objectMapper.readValue(json, AnimalNotificationUserSubscribedEvent.class);

    consumer.listen(message);

    verify(animalTopicPendingSubscriptionService, never()).saveSubscriber(anyString(), anyString());
  }
}
