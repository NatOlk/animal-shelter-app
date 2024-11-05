package com.ansh.notification;

import com.ansh.event.subscription.AnimalNotificationUserSubscribedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class AnimalNotificationUserSubscribedProducer {

  private final static Logger LOG = LoggerFactory.getLogger(
      AnimalNotificationUserSubscribedProducer.class);
  @Value("${approveTopicId}")
  private String approveTopicId;

  private final KafkaTemplate<String, String> kafkaTemplate;
  private final ObjectMapper objectMapper;

  public AnimalNotificationUserSubscribedProducer(KafkaTemplate<String, String> kafkaTemplate,
      ObjectMapper objectMapper) {
    this.kafkaTemplate = kafkaTemplate;
    this.objectMapper = objectMapper;
  }

  public void sendApprove(String email, String approver, String topic) {
    try {
      AnimalNotificationUserSubscribedEvent animalNotificationUserSubscribedEvent =
          new AnimalNotificationUserSubscribedEvent();
      animalNotificationUserSubscribedEvent.setEmail(email);
      animalNotificationUserSubscribedEvent.setApprover(approver);
      animalNotificationUserSubscribedEvent.setTopic(topic);
      String jsonMessage = objectMapper.writeValueAsString(animalNotificationUserSubscribedEvent);

      kafkaTemplate.send(approveTopicId, jsonMessage);
    } catch (Exception e) {
      LOG.error("Exception during sending message:", e.getMessage());
    }
  }
}
