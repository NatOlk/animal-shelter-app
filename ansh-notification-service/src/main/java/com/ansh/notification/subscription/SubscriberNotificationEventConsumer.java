package com.ansh.notification.subscription;

import com.ansh.event.subscription.SubscriptionDecisionEvent;
import com.ansh.service.SubscriberRegistryService;
import com.ansh.utils.IdentifierMasker;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SubscriberNotificationEventConsumer {

  private static final Logger LOG = LoggerFactory.getLogger(SubscriberNotificationEventConsumer.class);

  private final ObjectMapper objectMapper = new ObjectMapper();
  private final Map<String, SubscriberRegistryService> topicToServiceMap = new HashMap<>();

  @Autowired
  public SubscriberNotificationEventConsumer(List<SubscriberRegistryService> services) {
    for (SubscriberRegistryService service : services) {
      topicToServiceMap.put(service.getTopicId(), service);
    }
  }

  @KafkaListener(topics = "${approveTopicId}", groupId = "notificationGroupId")
  public void listen(ConsumerRecord<String, String> message) throws IOException {
    processMessage(message);
  }

  private void processMessage(ConsumerRecord<String, String> message) throws IOException {
    SubscriptionDecisionEvent event = objectMapper.readValue(message.value(), SubscriptionDecisionEvent.class);
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

    SubscriberRegistryService service = topicToServiceMap.get(topic);
    if (service == null) {
      LOG.error("[approve topic] No subscriber registry service found for topic {}", topic);
      return;
    }

    service.handleSubscriptionApproval(email, approver, reject);
  }
}