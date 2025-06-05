package com.ansh.notification.subscription;

import com.ansh.event.subscription.SubscriptionDecisionEvent;
import com.ansh.facade.SubscriptionFacade;
import com.ansh.utils.IdentifierMasker;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
public class SubscriberNotificationEventConsumer {

  private static final Logger LOG = LoggerFactory.getLogger(
      SubscriberNotificationEventConsumer.class);

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Autowired
  private SubscriptionFacade subscriptionFacade;

  @KafkaListener(topics = "${approveTopicId}",
      groupId = "notificationGroupId",
      containerFactory = "manualAckFactory")
  public void listen(ConsumerRecord<String, String> message, Acknowledgment ack) {
    processMessage(message, ack);
  }

  private void processMessage(ConsumerRecord<String, String> message, Acknowledgment ack) {
    try {
      SubscriptionDecisionEvent event = objectMapper.readValue(message.value(),
          SubscriptionDecisionEvent.class);
      String topic = event.getTopic();
      String email = event.getEmail();
      String approver = event.getApprover();
      boolean reject = event.isReject();

      if (LOG.isDebugEnabled()) {
        LOG.debug(
            "[approve topic] Received event: topic={}, subscriber={}, approver={}, isReject={}",
            topic,
            IdentifierMasker.maskEmail(email),
            IdentifierMasker.maskEmail(approver),
            reject
        );
      }

      subscriptionFacade.handleSubscriptionApproval(email, approver, topic, reject);
      ack.acknowledge();
    } catch (IllegalArgumentException ex) {
      LOG.error("Invalid topic in message: {}, skipping", message.value(), ex);
      ack.acknowledge();
    } catch (Exception ex) {
      LOG.error("Failed to process Kafka message {}", ex.toString());
    }
  }
}