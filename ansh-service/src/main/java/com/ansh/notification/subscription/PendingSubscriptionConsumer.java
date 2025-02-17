package com.ansh.notification.subscription;

import com.ansh.app.service.notification.subscription.PendingSubscriptionService;
import com.ansh.event.subscription.SubscriptionDecisionEvent;
import com.ansh.notification.strategy.PendingSubscriptionServiceStrategy;
import com.ansh.utils.IdentifierMasker;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Optional;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PendingSubscriptionConsumer {

  private static final Logger LOG = LoggerFactory.getLogger(PendingSubscriptionConsumer.class);

  @Value("${subscriptionTopicId}")
  private String subscriptionTopicId;
  @Value("${animalTopicId}")
  private String animalTopicId;

  @Autowired
  private PendingSubscriptionServiceStrategy serviceStrategy;
  @Autowired
  private ObjectMapper objectMapper;

  @KafkaListener(topics = "${subscriptionTopicId}", groupId = "animalGroupId")
  public void listen(ConsumerRecord<String, String> message) {
    try {
      SubscriptionDecisionEvent event = deserializeMessage(message.value());
      processEvent(event);
    } catch (IOException e) {
      LOG.error("Error of message deserialization: {}", message.value(), e);
    }
  }

  private SubscriptionDecisionEvent deserializeMessage(String message) throws IOException {
    return objectMapper.readValue(message, SubscriptionDecisionEvent.class);
  }

  private void processEvent(SubscriptionDecisionEvent event) {
    Optional<PendingSubscriptionService> service = serviceStrategy.getServiceByTopic(
        event.getTopic());

    service.ifPresentOrElse(
        s -> {
          s.saveSubscriber(event.getEmail(), event.getApprover());
          if (LOG.isDebugEnabled()) {
            LOG.debug("[{} subscription] Successfully processed subscription for {} by {}",
                event.getTopic(), IdentifierMasker.maskEmail(event.getEmail()),
                IdentifierMasker.maskEmail(event.getApprover()));
          }
        },
        () -> LOG.warn("[subscription consumer] No registered service for topic: {}",
            event.getTopic())
    );
  }

  protected void setSubscriptionTopicId(String subscriptionTopicId) {
    this.subscriptionTopicId = subscriptionTopicId;
  }

  protected void setAnimalTopicId(String animalTopicId) {
    this.animalTopicId = animalTopicId;
  }
}
