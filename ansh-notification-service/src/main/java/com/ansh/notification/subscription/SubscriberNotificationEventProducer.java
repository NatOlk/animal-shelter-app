package com.ansh.notification.subscription;

import com.ansh.event.subscription.AnimalNotificationUserSubscribedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class SubscriberNotificationEventProducer {

  private static final Logger LOG = LoggerFactory.getLogger(
      SubscriberNotificationEventProducer.class);

  @Value("${subscriptionTopicId}")
  private String subscriptionTopicId;
  private final KafkaTemplate<String, String> kafkaTemplate;
  private final ObjectMapper objectMapper;

  public SubscriberNotificationEventProducer(KafkaTemplate<String, String> kafkaTemplate,
      ObjectMapper objectMapper) {
    this.kafkaTemplate = kafkaTemplate;
    this.objectMapper = objectMapper;
  }

  public void sendApproveRequest(String email, String approver, String topic) {
    try {
      AnimalNotificationUserSubscribedEvent event =
          new AnimalNotificationUserSubscribedEvent();
      event.setEmail(email);
      event.setApprover(approver);
      event.setTopic(topic);
      String jsonMessage = objectMapper.writeValueAsString(event);

      kafkaTemplate.send(subscriptionTopicId, jsonMessage);
    } catch (Exception e) {
      LOG.error("Exception during sending message: {}", e.getMessage());
    }
  }

  protected void setSubscriptionTopicId(String subscriptionTopicId) {
    this.subscriptionTopicId = subscriptionTopicId;
  }
}
