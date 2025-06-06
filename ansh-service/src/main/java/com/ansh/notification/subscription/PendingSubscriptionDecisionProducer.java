package com.ansh.notification.subscription;

import com.ansh.event.subscription.SubscriptionDecisionEvent;
import com.ansh.exception.SendNotificationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class PendingSubscriptionDecisionProducer {

  private static final Logger LOG = LoggerFactory.getLogger(
      PendingSubscriptionDecisionProducer.class);

  @Value("${approveTopicId}")
  private String approveTopicId;

  private final KafkaTemplate<String, String> kafkaTemplate;
  private final ObjectMapper objectMapper;

  public PendingSubscriptionDecisionProducer(KafkaTemplate<String, String> kafkaTemplate,
      ObjectMapper objectMapper) {
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
      SubscriptionDecisionEvent event = SubscriptionDecisionEvent.builder()
          .email(email)
          .approver(approver)
          .topic(topic)
          .reject(isReject)
          .build();

      String jsonMessage = objectMapper.writeValueAsString(event);
      kafkaTemplate.send(approveTopicId, jsonMessage);

      LOG.debug("[send decision topic] sent message {}", jsonMessage);
    } catch (Exception e) {
      LOG.error("[send decision topic] exception during sending message {}", e.getMessage(), e);
      throw new SendNotificationException(
          STR."Can't send notification exception: \{e.getMessage()}");
    }
  }

  protected void setApproveTopicId(String approveTopicId) {
    this.approveTopicId = approveTopicId;
  }
}
