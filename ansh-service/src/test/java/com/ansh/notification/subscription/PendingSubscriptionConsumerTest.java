package com.ansh.notification.subscription;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ansh.app.service.notification.subscription.PendingSubscriptionService;
import com.ansh.event.subscription.SubscriptionDecisionEvent;
import com.ansh.notification.strategy.PendingSubscriptionServiceStrategy;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Optional;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class PendingSubscriptionConsumerTest {

  private static final String SUBSCRIPTION_TOPIC = "subscriptionTopic";
  private static final String ANIMAL_TOPIC = "animalTopic";
  private static final String UNKNOWN_TOPIC = "unknownTopic";

  @Mock
  private PendingSubscriptionServiceStrategy serviceStrategy;

  @Mock
  private PendingSubscriptionService animalSubscriptionService;

  @Mock
  private ObjectMapper objectMapper;

  @InjectMocks
  private PendingSubscriptionConsumer consumer;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    consumer.setSubscriptionTopicId(SUBSCRIPTION_TOPIC);
  }

  @Test
  void listen_validMessage_shouldCallSaveSubscriber() throws Exception {
    String json = "{\"email\":\"test@example.com\", \"approver\":\"admin\", \"topic\":\"animalTopic\"}";
    ConsumerRecord<String, String> message = new ConsumerRecord<>(SUBSCRIPTION_TOPIC, 0, 0L, "key",
        json);

    SubscriptionDecisionEvent event = SubscriptionDecisionEvent.builder()
        .email("test@example.com")
        .approver("admin@example.com")
        .topic(ANIMAL_TOPIC)
        .build();

    when(objectMapper.readValue(json, SubscriptionDecisionEvent.class)).thenReturn(event);
    when(serviceStrategy.getServiceByTopic(ANIMAL_TOPIC)).thenReturn(
        Optional.of(animalSubscriptionService));

    consumer.listen(message);

    verify(objectMapper, times(1)).readValue(json, SubscriptionDecisionEvent.class);
    verify(serviceStrategy, times(1)).getServiceByTopic(ANIMAL_TOPIC);
    verify(animalSubscriptionService, times(1)).saveSubscriber(event.getEmail(),
        event.getApprover());
  }

  @Test
  void listen_invalidMessage_shouldNotCallSaveSubscriber() throws Exception {
    String invalidJson = "{\"invalidField\":\"value\"}";
    ConsumerRecord<String, String> message = new ConsumerRecord<>(SUBSCRIPTION_TOPIC, 0, 0L, "key",
        invalidJson);

    doAnswer(invocation -> {
      throw new IOException("Invalid JSON");
    }).when(objectMapper).readValue(anyString(), eq(SubscriptionDecisionEvent.class));

    consumer.listen(message);

    verify(objectMapper, times(1)).readValue(anyString(), eq(SubscriptionDecisionEvent.class));
    verify(serviceStrategy, never()).getServiceByTopic(anyString());
    verify(animalSubscriptionService, never()).saveSubscriber(anyString(), anyString());
  }


  @Test
  void listen_unknownTopic_shouldNotCallSaveSubscriber() throws Exception {
    String json = "{\"email\":\"test@example.com\", \"approver\":\"admin\", \"topic\":\"unknownTopic\"}";
    ConsumerRecord<String, String> message = new ConsumerRecord<>(SUBSCRIPTION_TOPIC, 0, 0L, "key",
        json);

    SubscriptionDecisionEvent event = SubscriptionDecisionEvent.builder()
        .email("test@example.com")
        .approver("admin@example.com")
        .topic(UNKNOWN_TOPIC)
        .build();

    when(objectMapper.readValue(json, SubscriptionDecisionEvent.class)).thenReturn(event);
    when(serviceStrategy.getServiceByTopic(UNKNOWN_TOPIC)).thenReturn(Optional.empty());

    consumer.listen(message);

    verify(objectMapper, times(1)).readValue(json, SubscriptionDecisionEvent.class);
    verify(serviceStrategy, times(1)).getServiceByTopic(UNKNOWN_TOPIC);
    verify(animalSubscriptionService, never()).saveSubscriber(anyString(), anyString());
  }
}
