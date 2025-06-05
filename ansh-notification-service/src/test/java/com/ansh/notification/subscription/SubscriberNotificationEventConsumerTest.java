package com.ansh.notification.subscription;

import static org.mockito.Mockito.*;

import com.ansh.event.AnimalShelterTopic;
import com.ansh.event.subscription.SubscriptionDecisionEvent;
import com.ansh.facade.SubscriptionFacade;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.support.Acknowledgment;

class SubscriberNotificationEventConsumerTest {

  private static final String ANIMAL_TOPIC = AnimalShelterTopic.ANIMAL_INFO.getTopicName();
  private static final String TEST_EMAIL = "test@test.com";
  private static final String APPROVER_EMAIL = "approver@test.com";

  @Mock
  private ObjectMapper objectMapper;

  @Mock
  private SubscriptionFacade subscriptionFacade;

  @Mock
  private Acknowledgment ack;

  @InjectMocks
  private SubscriberNotificationEventConsumer consumer;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void shouldHandleSubscriptionApprovalViaFacade() throws Exception {
    // given
    SubscriptionDecisionEvent event = SubscriptionDecisionEvent.builder()
        .topic(ANIMAL_TOPIC)
        .email(TEST_EMAIL)
        .approver(APPROVER_EMAIL)
        .reject(false)
        .build();

    String json = "{\"topic\":\"%s\",\"email\":\"%s\",\"approver\":\"%s\",\"reject\":false}"
        .formatted(ANIMAL_TOPIC, TEST_EMAIL, APPROVER_EMAIL);
    ConsumerRecord<String, String> record = new ConsumerRecord<>(ANIMAL_TOPIC, 0, 0L,
        "key", json);

    when(objectMapper.readValue(json, SubscriptionDecisionEvent.class)).thenReturn(event);

    // when
    consumer.listen(record, ack);

    // then
    verify(subscriptionFacade, times(1))
        .handleSubscriptionApproval(TEST_EMAIL, APPROVER_EMAIL, ANIMAL_TOPIC, false);
    verify(ack).acknowledge();
  }

  @Test
  void shouldLogWarningAndAcknowledgeIfIllegalArgument() throws Exception {
    // given
    SubscriptionDecisionEvent event = SubscriptionDecisionEvent.builder()
        .topic("unsupported-topic")
        .email(TEST_EMAIL)
        .approver(APPROVER_EMAIL)
        .reject(true)
        .build();

    String json = "{\"topic\":\"unsupported-topic\",\"email\":\"%s\",\"approver\":\"%s\",\"reject\":true}"
        .formatted(TEST_EMAIL, APPROVER_EMAIL);
    ConsumerRecord<String, String> record = new ConsumerRecord<>("unsupported-topic", 0,
        0L, "key", json);

    when(objectMapper.readValue(json, SubscriptionDecisionEvent.class)).thenReturn(event);
    doThrow(new IllegalArgumentException("No service found for topic"))
        .when(subscriptionFacade)
        .handleSubscriptionApproval(TEST_EMAIL, APPROVER_EMAIL, "unsupported-topic", true);

    // when
    consumer.listen(record, ack);

    // then
    verify(subscriptionFacade).handleSubscriptionApproval(TEST_EMAIL, APPROVER_EMAIL,
        "unsupported-topic", true);
    verify(ack).acknowledge();
  }

  @Test
  void shouldNotAcknowledgeOnInvalidJson() throws Exception {
    // given
    String invalidJson = "not a valid json";
    ConsumerRecord<String, String> record = new ConsumerRecord<>(ANIMAL_TOPIC, 0, 0L,
        "key", invalidJson);

    when(objectMapper.readValue(invalidJson, SubscriptionDecisionEvent.class))
        .thenThrow(new JsonProcessingException("Invalid JSON") {});

    // when
    consumer.listen(record, ack);

    // then
    verifyNoInteractions(subscriptionFacade);
    verify(ack, never()).acknowledge();
  }
}