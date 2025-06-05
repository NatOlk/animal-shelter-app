package com.ansh.notification.subscription;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ansh.app.facade.PendingSubscriptionFacade;
import com.ansh.event.subscription.SubscriptionDecisionEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.support.Acknowledgment;

class PendingSubscriptionConsumerTest {

  private static final String SUBSCRIPTION_TOPIC = "subscriptionTopic";
  private static final String ANIMAL_TOPIC = "animalTopic";
  private static final String UNKNOWN_TOPIC = "unknownTopic";

  @Mock
  private PendingSubscriptionFacade pendingSubscriptionFacade;

  @Mock
  private ObjectMapper objectMapper;

  @Mock
  private Acknowledgment ack;

  @InjectMocks
  private PendingSubscriptionConsumer consumer;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    consumer.setSubscriptionTopicId(SUBSCRIPTION_TOPIC);
  }

  @Test
  void listen_validMessage_shouldCallFacadeSaveSubscriber() throws Exception {
    // given
    String json = "{\"email\":\"test@example.com\", \"approver\":\"admin@example.com\","
        + " \"topic\":\"animalTopic\"}";
    ConsumerRecord<String, String> message = new ConsumerRecord<>(SUBSCRIPTION_TOPIC,
        0, 0L, "key", json);

    SubscriptionDecisionEvent event = SubscriptionDecisionEvent.builder()
        .email("test@example.com")
        .approver("admin@example.com")
        .topic(ANIMAL_TOPIC)
        .build();

    when(objectMapper.readValue(json, SubscriptionDecisionEvent.class)).thenReturn(event);

    // when
    consumer.listen(message, ack);

    // then
    verify(objectMapper).readValue(json, SubscriptionDecisionEvent.class);
    verify(pendingSubscriptionFacade).saveSubscriber(ANIMAL_TOPIC, "test@example.com",
        "admin@example.com");
    verify(ack).acknowledge();
  }

  @Test
  void listen_invalidJson_shouldNotCallFacadeAndNotAcknowledge() throws Exception {
    // given
    String invalidJson = "{\"bad\":\"data\"}";
    ConsumerRecord<String, String> message = new ConsumerRecord<>(SUBSCRIPTION_TOPIC, 0, 0L, "key",
        invalidJson);

    doThrow(new JsonProcessingException("Invalid JSON") {
    })
        .when(objectMapper)
        .readValue(anyString(), eq(SubscriptionDecisionEvent.class));

    // when
    consumer.listen(message, ack);

    // then
    verify(objectMapper).readValue(anyString(), eq(SubscriptionDecisionEvent.class));
    verify(pendingSubscriptionFacade, never()).saveSubscriber(anyString(), anyString(),
        anyString());
    verify(ack, never()).acknowledge();
  }

  @Test
  void listen_unexpectedException_shouldNotAcknowledge() throws Exception {
    // given
    String json = "{\"email\":\"test@example.com\", \"approver\":\"admin@example.com\", \"topic\":\"animalTopic\"}";
    ConsumerRecord<String, String> message = new ConsumerRecord<>(SUBSCRIPTION_TOPIC, 0, 0L, "key",
        json);

    SubscriptionDecisionEvent event = SubscriptionDecisionEvent.builder()
        .email("test@example.com")
        .approver("admin@example.com")
        .topic(ANIMAL_TOPIC)
        .build();

    when(objectMapper.readValue(json, SubscriptionDecisionEvent.class)).thenReturn(event);
    doThrow(new RuntimeException("Unexpected")).when(pendingSubscriptionFacade)
        .saveSubscriber(anyString(), anyString(), anyString());

    // when
    consumer.listen(message, ack);

    // then
    verify(pendingSubscriptionFacade).saveSubscriber(ANIMAL_TOPIC, "test@example.com",
        "admin@example.com");
    verify(ack, never()).acknowledge();
  }

  @Test
  void listen_shouldNotAckIfFacadeThrowsIllegalArgumentException() throws Exception {
    // given
    String json = "{\"email\":\"test@example.com\", \"approver\":\"admin\", \"topic\":\"unknownTopic\"}";
    ConsumerRecord<String, String> message = new ConsumerRecord<>(SUBSCRIPTION_TOPIC, 0, 0L, "key",
        json);

    SubscriptionDecisionEvent event = SubscriptionDecisionEvent.builder()
        .email("test@example.com")
        .approver("admin")
        .topic(UNKNOWN_TOPIC)
        .build();

    when(objectMapper.readValue(json, SubscriptionDecisionEvent.class)).thenReturn(event);
    doThrow(new IllegalArgumentException("No service found")).when(pendingSubscriptionFacade)
        .saveSubscriber(UNKNOWN_TOPIC, "test@example.com", "admin");

    // when
    consumer.listen(message, ack);

    // then
    verify(objectMapper).readValue(json, SubscriptionDecisionEvent.class);
    verify(pendingSubscriptionFacade).saveSubscriber(UNKNOWN_TOPIC, "test@example.com", "admin");
    verify(ack, times(1)).acknowledge();
  }
}
