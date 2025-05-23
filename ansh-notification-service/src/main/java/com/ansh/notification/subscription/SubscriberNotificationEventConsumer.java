package com.ansh.notification.subscription;

import com.ansh.event.subscription.SubscriptionDecisionEvent;
import com.ansh.strategy.SubscriberRegistryServiceStrategy;
import com.ansh.utils.IdentifierMasker;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class SubscriberNotificationEventConsumer {

  private static final Logger LOG = LoggerFactory.getLogger(SubscriberNotificationEventConsumer.class);

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Autowired
  private SubscriberRegistryServiceStrategy subscriberRegistryServiceStrategy;

  @KafkaListener(topics = "${approveTopicId}", groupId = "notificationGroupId")
  public void listen(ConsumerRecord<String, String> message) throws IOException {
    processMessage(message);
  }

  private void processMessage(ConsumerRecord<String, String> message) throws IOException {
    SubscriptionDecisionEvent event = objectMapper.readValue(message.value(),
        SubscriptionDecisionEvent.class);
    String topic = event.getTopic();
    String email = event.getEmail();
    String approver = event.getApprover();
    boolean reject = event.isReject();

    if (LOG.isDebugEnabled()) {
      LOG.debug("[approve topic] Received event: topic={}, subscriber={}, approver={}, isReject={}",
          topic,
          IdentifierMasker.maskEmail(email),
          IdentifierMasker.maskEmail(approver),
          reject
      );
    }

    subscriberRegistryServiceStrategy.getServiceByTopic(topic)
        .ifPresentOrElse(
            service -> service.handleSubscriptionApproval(email, approver, reject),
            () -> LOG.warn("No service found for topic: {}", topic)
        );
  }

  protected void setSubscriberRegistryServiceStrategy(SubscriberRegistryServiceStrategy serviceStrategy) {
    this.subscriberRegistryServiceStrategy = serviceStrategy;
  }
}