package com.ansh.notification;

import com.ansh.event.subscription.AnimalNotificationUserSubscribedEvent;
import com.ansh.service.AnimalTopicSubscriberRegistryService;
import com.ansh.utils.IdentifierMasker;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class SubscriberNotificationInfoConsumer {

  private static final Logger LOG = LoggerFactory.getLogger(
      SubscriberNotificationInfoConsumer.class);

  @Value("${approveTopicId}")
  private String approveTopicId;

  @Autowired
  private AnimalTopicSubscriberRegistryService topicSubscriberRegistryService;

  @KafkaListener(topics = "${approveTopicId}", groupId = "animalGroupId")
  public void listen(ConsumerRecord<String, String> message) throws IOException {

    AnimalNotificationUserSubscribedEvent animalEvent = new ObjectMapper().readValue(
        message.value(),
        AnimalNotificationUserSubscribedEvent.class);
    String email = animalEvent.getEmail();
    String approver = animalEvent.getApprover();
    boolean reject = animalEvent.isReject();
    LOG.debug("[approve topic] subscriber {} with approver {} isReject? {}",
        IdentifierMasker.maskEmail(email),
        IdentifierMasker.maskEmail(approver), reject);
    topicSubscriberRegistryService.handleSubscriptionApproval(email, approver, reject);
  }
}
