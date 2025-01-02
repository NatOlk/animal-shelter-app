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

  private static final Logger LOG = LoggerFactory.getLogger(AnimalNotificationUserSubscribedProducer.class);

  @Value("${approveTopicId}")
  private String approveTopicId;

  private final KafkaTemplate<String, String> kafkaTemplate;
  private final ObjectMapper objectMapper;

  public AnimalNotificationUserSubscribedProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
    this.kafkaTemplate = kafkaTemplate;
    this.objectMapper = objectMapper;
  }

  public void sendApprove(String email, String approver, String topic) {
    sendEvent(email, approver, topic, false);
  }

  public void sendReject(String email, String approver, String topic) {
    sendEvent(email, approver, topic, true);
  }

  private void sendEvent(String email, String approver, String topic, boolean isReject) {
    try {
      AnimalNotificationUserSubscribedEvent event = new AnimalNotificationUserSubscribedEvent();
      event.setEmail(email);
      event.setApprover(approver);
      event.setTopic(topic);
      event.setReject(isReject);

      String jsonMessage = objectMapper.writeValueAsString(event);
      kafkaTemplate.send(approveTopicId, jsonMessage);

      LOG.debug("Message sent to topic {}: {}", approveTopicId, jsonMessage);
    } catch (Exception e) {
      LOG.error("Exception during sending message to topic {}: {}", approveTopicId, e.getMessage(), e);
    }
  }

  protected void setApproveTopicId(String approveTopicId) {
    this.approveTopicId = approveTopicId;
  }
}
