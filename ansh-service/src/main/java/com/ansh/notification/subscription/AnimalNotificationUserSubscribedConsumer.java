package com.ansh.notification.subscription;

import com.ansh.app.service.notification.subscription.AnimalInfoPendingSubscriptionService;
import com.ansh.event.subscription.AnimalNotificationUserSubscribedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class AnimalNotificationUserSubscribedConsumer {

  private static final Logger LOG = LoggerFactory.getLogger(
      AnimalNotificationUserSubscribedConsumer.class);

  @Value("${subscriptionTopicId}")
  private String subscriptionTopicId;
  @Value("${animalTopicId}")
  private String animalTopicId;

  @Autowired
  @Qualifier("animalInfoPendingSubscriptionService")
  private AnimalInfoPendingSubscriptionService pendingSubscriptionService;

  @KafkaListener(topics = "${subscriptionTopicId}", groupId = "animalGroupId")
  public void listen(ConsumerRecord<String, String> message) {
    try {
      AnimalNotificationUserSubscribedEvent event =
          new ObjectMapper().readValue(message.value(),
              AnimalNotificationUserSubscribedEvent.class);
      //At the moment only animalTopic subscription is possible
      if (animalTopicId.equals(event.getTopic())) {
        pendingSubscriptionService.saveSubscriber(event.getEmail(),
            event.getApprover());
      }
    } catch (IOException e) {
      LOG.error("Error of message deserialization: {}", message.value(), e);
    }
  }


  protected void setSubscriptionTopicId(String subscriptionTopicId) {
    this.subscriptionTopicId = subscriptionTopicId;
  }

  protected void setAnimalTopicId(String animalTopicId) {
    this.animalTopicId = animalTopicId;
  }
}
