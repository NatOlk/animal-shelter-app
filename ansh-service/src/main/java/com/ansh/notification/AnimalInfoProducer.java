package com.ansh.notification;

import com.ansh.event.AnimalEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class AnimalInfoProducer {

  private static final Logger LOG = LoggerFactory.getLogger(AnimalInfoProducer.class);
  @Value("${animalTopicId}")
  private String animalTopicId;

  private final KafkaTemplate<String, String> kafkaTemplate;
  private final ObjectMapper objectMapper;

  public AnimalInfoProducer(KafkaTemplate<String, String> kafkaTemplate,
      ObjectMapper objectMapper) {
    this.kafkaTemplate = kafkaTemplate;
    this.objectMapper = objectMapper;
  }

  public void sendMessage(AnimalEvent animalEvent) {
    try {
      String jsonMessage = objectMapper.writeValueAsString(animalEvent);
      kafkaTemplate.send(animalTopicId, jsonMessage);
    } catch (Exception e) {
      LOG.error("Exception during sending message:", e.getMessage());
    }
  }
}
