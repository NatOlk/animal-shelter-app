package com.ansh.notification;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ansh.event.subscription.AnimalNotificationUserSubscribedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

class SubscriberNotificationInfoProducerTest {

  private static final String SUBSCRIPTION_TOPIC = "subscriptionTopicId";

  @Mock
  private KafkaTemplate<String, String> kafkaTemplate;

  @Mock
  private ObjectMapper objectMapper;

  @InjectMocks
  private SubscriberNotificationInfoProducer subscriberNotificationInfoProducer;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    subscriberNotificationInfoProducer.setSubscriptionTopicId(SUBSCRIPTION_TOPIC);
  }

  @Test
  void testSendApproveRequest() throws Exception {

    String email = "test@example.com";
    String approver = "admin";
    String topic = "animal_notifications";

    AnimalNotificationUserSubscribedEvent event = new AnimalNotificationUserSubscribedEvent();
    event.setEmail(email);
    event.setApprover(approver);
    event.setTopic(topic);

    String jsonMessage = "{\"email\":\"test@example.com\", \"approver\":\"admin\", \"topic\":\"animal_notifications\"}";

    when(objectMapper.writeValueAsString(event)).thenReturn(jsonMessage);

    subscriberNotificationInfoProducer.sendApproveRequest(email, approver, topic);

    verify(kafkaTemplate, times(1)).send(eq(SUBSCRIPTION_TOPIC), eq(jsonMessage));
  }

  @Test
  void testSendApproveRequestExceptionHandling() throws Exception {

    String email = "test@example.com";
    String approver = "admin";
    String topic = "animal_notifications";

    AnimalNotificationUserSubscribedEvent event = new AnimalNotificationUserSubscribedEvent();
    event.setEmail(email);
    event.setApprover(approver);
    event.setTopic(topic);

    when(objectMapper.writeValueAsString(event)).thenThrow(new RuntimeException("Test exception"));

    subscriberNotificationInfoProducer.sendApproveRequest(email, approver, topic);

    verify(kafkaTemplate, never()).send(eq(SUBSCRIPTION_TOPIC), anyString());
  }
}
