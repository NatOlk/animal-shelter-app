package com.ansh.notification.subscription;

import static org.mockito.Mockito.*;

import com.ansh.app.service.notification.subscription.PendingSubscriptionService;
import com.ansh.event.subscription.SubscriptionDecisionEvent;
import com.ansh.notification.strategy.PendingSubscriptionServiceStrategy;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

class PendingSubscriptionConsumerTest {

  private static final String SUBSCRIPTION_TOPIC = "subscriptionTopic";
  private static final String ANIMAL_TOPIC = "animalTopic";
  private static final String UNKNOWN_TOPIC = "unknownTopic";

  @Mock
  private PendingSubscriptionServiceStrategy serviceStrategy;

  @Mock
  private PendingSubscriptionService animalSubscriptionService;

  @InjectMocks
  private PendingSubscriptionConsumer consumer;

  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    consumer.setSubscriptionTopicId(SUBSCRIPTION_TOPIC);
    consumer.setAnimalTopicId(ANIMAL_TOPIC);
    objectMapper = new ObjectMapper();
  }

  @Test
  void listen_validMessage_shouldCallSaveSubscriber() throws Exception {
    String json = "{\"email\":\"test@example.com\", \"approver\":\"admin\", \"topic\":\"animalTopic\"}";
    ConsumerRecord<String, String> message = new ConsumerRecord<>(SUBSCRIPTION_TOPIC, 0, 0L, "key", json);

    SubscriptionDecisionEvent event = objectMapper.readValue(json, SubscriptionDecisionEvent.class);

    when(serviceStrategy.getServiceByTopic(ANIMAL_TOPIC)).thenReturn(Optional.of(animalSubscriptionService));

    consumer.listen(message);

    verify(serviceStrategy, times(1)).getServiceByTopic(ANIMAL_TOPIC);
    verify(animalSubscriptionService, times(1)).saveSubscriber(event.getEmail(), event.getApprover());
  }

  @Test
  void listen_invalidMessage_shouldNotCallSaveSubscriber() {
    String invalidJson = "{\"invalidField\":\"value\"}";
    ConsumerRecord<String, String> message = new ConsumerRecord<>(SUBSCRIPTION_TOPIC, 0, 0L, "key", invalidJson);

    consumer.listen(message);

    verify(serviceStrategy, never()).getServiceByTopic(anyString());
    verify(animalSubscriptionService, never()).saveSubscriber(anyString(), anyString());
  }

  @Test
  void listen_unknownTopic_shouldNotCallSaveSubscriber() throws Exception {
    String json = "{\"email\":\"test@example.com\", \"approver\":\"admin\", \"topic\":\"unknownTopic\"}";
    ConsumerRecord<String, String> message = new ConsumerRecord<>(SUBSCRIPTION_TOPIC, 0, 0L, "key", json);

    SubscriptionDecisionEvent event = objectMapper.readValue(json, SubscriptionDecisionEvent.class);

    when(serviceStrategy.getServiceByTopic(UNKNOWN_TOPIC)).thenReturn(Optional.empty());

    consumer.listen(message);

    verify(serviceStrategy, times(1)).getServiceByTopic(UNKNOWN_TOPIC);
    verify(animalSubscriptionService, never()).saveSubscriber(anyString(), anyString());
  }
}
