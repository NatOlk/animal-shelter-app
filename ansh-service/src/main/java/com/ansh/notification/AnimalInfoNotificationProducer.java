package com.ansh.notification;

import com.ansh.event.AnimalEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class AnimalInfoNotificationProducer {

  private static final Logger LOG = LoggerFactory.getLogger(AnimalInfoNotificationProducer.class);
  @Value("${animalTopicId}")
  private String animalTopicId;

  private final KafkaTemplate<String, String> kafkaTemplate;
  private final ObjectMapper objectMapper;

  public AnimalInfoNotificationProducer(KafkaTemplate<String, String> kafkaTemplate,
      ObjectMapper objectMapper) {
    this.kafkaTemplate = kafkaTemplate;
    this.objectMapper = objectMapper;
  }

  public void sendMessage(AnimalEvent animalEvent) {
    try {
      String jsonMessage = objectMapper.writeValueAsString(animalEvent);
      kafkaTemplate.send(animalTopicId, jsonMessage);
      LOG.debug("[animal topic] sent message {}", jsonMessage);
    } catch (Exception e) {
      LOG.error("[animal topic] exception during sending message: {}", e.getMessage());
    }
  }

  protected void setAnimalTopicId(String animalTopicId) {
    this.animalTopicId = animalTopicId;
  }
}
