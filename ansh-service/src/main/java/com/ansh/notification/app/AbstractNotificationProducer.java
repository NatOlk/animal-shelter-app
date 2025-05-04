package com.ansh.notification.app;

import com.ansh.event.AnimalShelterEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;

public abstract class AbstractNotificationProducer implements AnimalShelterNotificationProducer {

  protected final KafkaTemplate<String, String> kafkaTemplate;
  protected final ObjectMapper objectMapper;

  protected abstract String getLogPrefix();

  protected AbstractNotificationProducer(KafkaTemplate<String, String> kafkaTemplate,
      ObjectMapper objectMapper) {
    this.kafkaTemplate = kafkaTemplate;
    this.objectMapper = objectMapper;
  }

  @Override
  public void sendNotification(AnimalShelterEvent event) {
    try {
      String jsonMessage = objectMapper.writeValueAsString(event);
      kafkaTemplate.send(getTopicId(), jsonMessage);
      LoggerFactory.getLogger(this.getClass())
          .debug("{} sent message: {}", getLogPrefix(), jsonMessage);
    } catch (Exception e) {
      LoggerFactory.getLogger(this.getClass())
          .error("{} exception during sending message: {}", getLogPrefix(), e.getMessage());
    }
  }
}

