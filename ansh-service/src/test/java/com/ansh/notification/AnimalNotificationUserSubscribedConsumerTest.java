package com.ansh.notification;

import static org.mockito.Mockito.*;

import com.ansh.event.subscription.AnimalNotificationUserSubscribedEvent;
import com.ansh.uimanagement.service.SubscriptionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class AnimalNotificationUserSubscribedConsumerTest {

  private static final String SUBSCRIPTION_TOPIC = "subscriptionTopic";

  @Mock
  private SubscriptionService subscriptionService;

  @InjectMocks
  private AnimalNotificationUserSubscribedConsumer consumer;

  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    consumer.setSubscriptionTopicId(SUBSCRIPTION_TOPIC);
    objectMapper = new ObjectMapper();
  }

  @Test
  void listen_validMessage_shouldCallSavePendingSubscriber() throws Exception {
    String json = "{\"email\":\"test@example.com\", \"approver\":\"admin\", \"topic\":\"animal_notifications\"}";
    ConsumerRecord<String, String> message = new ConsumerRecord<>(SUBSCRIPTION_TOPIC, 0, 0L, "key", json);

    AnimalNotificationUserSubscribedEvent event = objectMapper.readValue(json, AnimalNotificationUserSubscribedEvent.class);

    consumer.listen(message);

    verify(subscriptionService, times(1))
        .savePendingSubscriber(event.getEmail(), event.getApprover(), event.getTopic());
  }

  @Test
  void listen_invalidMessage_shouldNotCallSavePendingSubscriber() {
    String invalidJSon = "{\"invalidField\":\"value\"}";
    ConsumerRecord<String, String> message = new ConsumerRecord<>(SUBSCRIPTION_TOPIC, 0, 0L, "key", invalidJSon);

    consumer.listen(message);

    verify(subscriptionService, never()).savePendingSubscriber(anyString(), anyString(), anyString());
  }
}
