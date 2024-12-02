package com.ansh.notification;

import com.ansh.event.subscription.AnimalNotificationUserSubscribedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class SubscriberNotificationInfoProducer {

  private static final Logger LOG = LoggerFactory.getLogger(
      SubscriberNotificationInfoProducer.class);

  @Value("${subscriptionTopicId}")
  private String subscriptionTopicId;

  private final KafkaTemplate<String, String> kafkaTemplate;
  private final ObjectMapper objectMapper;

  public SubscriberNotificationInfoProducer(KafkaTemplate<String, String> kafkaTemplate,
      ObjectMapper objectMapper) {
    this.kafkaTemplate = kafkaTemplate;
    this.objectMapper = objectMapper;
  }

  public void sendApproveRequest(String email, String approver, String topic) {
    try {
      AnimalNotificationUserSubscribedEvent animalNotificationUserSubscribedEvent =
          new AnimalNotificationUserSubscribedEvent();
      animalNotificationUserSubscribedEvent.setEmail(email);
      animalNotificationUserSubscribedEvent.setApprover(approver);
      animalNotificationUserSubscribedEvent.setTopic(topic);
      String jsonMessage = objectMapper.writeValueAsString(animalNotificationUserSubscribedEvent);

      kafkaTemplate.send(subscriptionTopicId, jsonMessage);
    } catch (Exception e) {
      LOG.error("Exception during sending message: {}", e.getMessage());
    }
  }

  protected void setSubscriptionTopicId(String subscriptionTopicId) {
    this.subscriptionTopicId = subscriptionTopicId;
  }
}
