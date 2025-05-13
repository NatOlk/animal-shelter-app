package com.ansh.notification.subscription;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ansh.event.subscription.SubscriptionDecisionEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

class SubscriberNotificationEventProducerTest {

  private static final String SUBSCRIPTION_TOPIC = "subscriptionTopicId";
  private static final String EMAIL = "test@example.com";
  private static final String APPROVER = "admin";
  private static final String TOPIC = "animal_notifications";

  @Mock
  private KafkaTemplate<String, String> kafkaTemplate;

  @Mock
  private ObjectMapper objectMapper;

  @InjectMocks
  private SubscriberNotificationEventProducer subscriberNotificationEventProducer;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    subscriberNotificationEventProducer.setSubscriptionTopicId(SUBSCRIPTION_TOPIC);
  }

  @Test
  void testSendApproveRequest_successfullySendsMessage() throws Exception {
    // given
    SubscriptionDecisionEvent event = new SubscriptionDecisionEvent();
    event.setEmail(EMAIL);
    event.setApprover(APPROVER);
    event.setTopic(TOPIC);

    String jsonMessage = "{\"email\":\"test@example.com\", \"approver\":\"admin\", \"topic\":\"animal_notifications\"}";
    when(objectMapper.writeValueAsString(event)).thenReturn(jsonMessage);

    // when
    subscriberNotificationEventProducer.sendPendingApproveRequest(EMAIL, APPROVER, TOPIC);

    // then
    verify(kafkaTemplate, times(1)).send(eq(SUBSCRIPTION_TOPIC), eq(jsonMessage));
  }

  @Test
  void testSendApproveRequest_whenSerializationFails_doesNotSendMessage() throws Exception {
    // given
    SubscriptionDecisionEvent event = new SubscriptionDecisionEvent();
    event.setEmail(EMAIL);
    event.setApprover(APPROVER);
    event.setTopic(TOPIC);

    when(objectMapper.writeValueAsString(event)).thenThrow(new RuntimeException("Test exception"));

    // when
    subscriberNotificationEventProducer.sendPendingApproveRequest(EMAIL, APPROVER, TOPIC);

    // then
    verify(kafkaTemplate, never()).send(eq(SUBSCRIPTION_TOPIC), anyString());
  }
}
