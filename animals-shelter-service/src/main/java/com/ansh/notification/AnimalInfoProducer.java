package com.ansh.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class AnimalInfoProducer {

  private static final Logger LOG = LoggerFactory.getLogger(AnimalInfoProducer.class);

  private final KafkaTemplate<String, String> kafkaTemplate;
  private final ObjectMapper objectMapper;

  public AnimalInfoProducer(KafkaTemplate<String, String> kafkaTemplate,
      ObjectMapper objectMapper) {
    this.kafkaTemplate = kafkaTemplate;
    this.objectMapper = objectMapper;
  }

  public void sendMessage(String topic, Map<String, Object> message) {
    try {
      String jsonMessage = objectMapper.writeValueAsString(message);
      kafkaTemplate.send(topic, jsonMessage);
    } catch (Exception e) {
      LOG.error("Exception during sending message:", e.getMessage());
    }
  }
}
