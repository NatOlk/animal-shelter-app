package com.ansh.notification.subscription;

import com.ansh.app.facade.PendingSubscriptionFacade;
import com.ansh.event.subscription.SubscriptionDecisionEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class PendingSubscriptionConsumer {

  private static final Logger LOG = LoggerFactory.getLogger(PendingSubscriptionConsumer.class);

  @Value("${subscriptionTopicId}")
  private String subscriptionTopicId;

  @Autowired
  private PendingSubscriptionFacade pendingSubscriptionFacade;

  @Autowired
  private ObjectMapper objectMapper;

  @KafkaListener(
      topics = "${subscriptionTopicId}",
      groupId = "animalGroupId",
      containerFactory = "manualAckFactory"
  )
  public void listen(ConsumerRecord<String, String> message, Acknowledgment ack) {
    try {
      SubscriptionDecisionEvent event = deserializeMessage(message.value());
      pendingSubscriptionFacade.saveSubscriber(event.getTopic(), event.getEmail(),
          event.getApprover());
      ack.acknowledge();
    } catch (IllegalArgumentException e) {
      LOG.error("Invalid topic in message: {}, skipping", message.value(), e);
      ack.acknowledge();
    } catch (IOException e) {
      LOG.error("Error of message deserialization: {}", message.value(), e);
    } catch (Exception e) {
      LOG.error("Unexpected error processing message: {}", message.value(), e);
    }
  }

  private SubscriptionDecisionEvent deserializeMessage(String message) throws IOException {
    return objectMapper.readValue(message, SubscriptionDecisionEvent.class);
  }

  protected void setSubscriptionTopicId(String subscriptionTopicId) {
    this.subscriptionTopicId = subscriptionTopicId;
  }
}