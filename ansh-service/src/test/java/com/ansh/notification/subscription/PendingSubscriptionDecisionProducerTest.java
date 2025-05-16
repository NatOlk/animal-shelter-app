package com.ansh.notification.subscription;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ansh.event.subscription.SubscriptionDecisionEvent;
import com.ansh.exception.SendNotificationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

class PendingSubscriptionDecisionProducerTest {

  private static final String TEST_TOPIC = "testApproveTopic";
  private static final String EMAIL = "user@example.com";
  private static final String APPROVER = "approverUser";
  private static final String TOPIC = "animal-topic";

  private static final String APPROVE_JSON =
      "{\"email\":\"user@example.com\",\"approver\":\"approverUser\",\"topic\":\"animal-topic\",\"reject\":false}";
  private static final String REJECT_JSON =
      "{\"email\":\"user@example.com\",\"approver\":\"approverUser\",\"topic\":\"animal-topic\",\"reject\":true}";

  @Mock
  private KafkaTemplate<String, String> kafkaTemplate;

  @Mock
  private ObjectMapper objectMapper;

  @InjectMocks
  private PendingSubscriptionDecisionProducer producer;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    producer = new PendingSubscriptionDecisionProducer(kafkaTemplate, objectMapper);
    producer.setApproveTopicId(TEST_TOPIC);
  }

  @Test
  void testSendApprove() throws JsonProcessingException {
    // given
    SubscriptionDecisionEvent event = SubscriptionDecisionEvent.builder()
        .email(EMAIL)
        .approver(APPROVER)
        .topic(TOPIC)
        .reject(false)
        .build();

    when(objectMapper.writeValueAsString(event)).thenReturn(APPROVE_JSON);

    // when
    producer.sendApprove(EMAIL, APPROVER, TOPIC);

    // then
    verify(objectMapper, times(1)).writeValueAsString(event);
    verify(kafkaTemplate, times(1)).send(TEST_TOPIC, APPROVE_JSON);
  }

  @Test
  void testSendReject() throws JsonProcessingException {
    // given
    SubscriptionDecisionEvent event = SubscriptionDecisionEvent.builder()
        .email(EMAIL)
        .approver(APPROVER)
        .topic(TOPIC)
        .reject(true)
        .build();

    when(objectMapper.writeValueAsString(event)).thenReturn(REJECT_JSON);

    // when
    producer.sendReject(EMAIL, APPROVER, TOPIC);

    // then
    verify(objectMapper, times(1)).writeValueAsString(event);
    verify(kafkaTemplate, times(1)).send(TEST_TOPIC, REJECT_JSON);
  }

  @Test
  void testSendEventWithException() throws JsonProcessingException {
    // given
    when(objectMapper.writeValueAsString(any(SubscriptionDecisionEvent.class)))
        .thenThrow(new JsonProcessingException("Error serializing object") {});

    // when
    assertThrows(SendNotificationException.class, () ->
        producer.sendApprove(EMAIL, APPROVER, TOPIC)
    );

    // then
    verify(kafkaTemplate, never()).send(anyString(), anyString());
    verify(objectMapper, times(1)).writeValueAsString(any(SubscriptionDecisionEvent.class));
  }
}