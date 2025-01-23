package com.ansh.notification.subscription;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ansh.event.subscription.AnimalNotificationUserSubscribedEvent;
import com.ansh.notification.subscription.AnimalNotificationUserSubscribedProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

class AnimalNotificationUserSubscribedProducerTest {

  private static final String TEST_TOPIC = "testApproveTopic";

  @Mock
  private KafkaTemplate<String, String> kafkaTemplate;

  @Mock
  private ObjectMapper objectMapper;

  @InjectMocks
  private AnimalNotificationUserSubscribedProducer producer;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    producer = new AnimalNotificationUserSubscribedProducer(kafkaTemplate, objectMapper);
    producer.setApproveTopicId(TEST_TOPIC);
  }

  @Test
  void testSendApprove() throws JsonProcessingException {
    String email = "user@example.com";
    String approver = "approverUser";
    String topic = "animal-topic";
    AnimalNotificationUserSubscribedEvent event = new AnimalNotificationUserSubscribedEvent();
    event.setEmail(email);
    event.setApprover(approver);
    event.setTopic(topic);
    event.setReject(false);

    String jsonMessage = "{\"email\":\"user@example.com\",\"approver\":\"approverUser\",\"topic\":\"animal-topic\",\"reject\":false}";
    when(objectMapper.writeValueAsString(event)).thenReturn(jsonMessage);

    producer.sendApprove(email, approver, topic);

    verify(objectMapper, times(1)).writeValueAsString(event);
    verify(kafkaTemplate, times(1)).send(TEST_TOPIC, jsonMessage);
  }

  @Test
  void testSendReject() throws JsonProcessingException {
    String email = "user@example.com";
    String approver = "approverUser";
    String topic = "animal-topic";
    AnimalNotificationUserSubscribedEvent event = new AnimalNotificationUserSubscribedEvent();
    event.setEmail(email);
    event.setApprover(approver);
    event.setTopic(topic);
    event.setReject(true);

    String jsonMessage = "{\"email\":\"user@example.com\",\"approver\":\"approverUser\",\"topic\":\"animal-topic\",\"reject\":true}";
    when(objectMapper.writeValueAsString(event)).thenReturn(jsonMessage);

    producer.sendReject(email, approver, topic);

    verify(objectMapper, times(1)).writeValueAsString(event);
    verify(kafkaTemplate, times(1)).send(TEST_TOPIC, jsonMessage);
  }

  @Test
  void testSendEventWithException() throws JsonProcessingException {
    String email = "user@example.com";
    String approver = "approverUser";
    String topic = "animal-topic";

    when(objectMapper.writeValueAsString(
        any(AnimalNotificationUserSubscribedEvent.class))).thenThrow(
        new JsonProcessingException("Error serializing object") {
        });

    producer.sendApprove(email, approver, topic);

    verify(kafkaTemplate, never()).send(anyString(), anyString());
    verify(objectMapper, times(1)).writeValueAsString(
        any(AnimalNotificationUserSubscribedEvent.class));
  }
}
