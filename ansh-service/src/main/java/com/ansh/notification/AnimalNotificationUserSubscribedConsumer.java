package com.ansh.notification;

import com.ansh.event.subscription.AnimalNotificationUserSubscribedEvent;
import com.ansh.uimanagement.service.SubscriptionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class AnimalNotificationUserSubscribedConsumer {

  @Value("${subscriptionTopicId}")
  private String subscriptionTopicId;
  @Autowired
  private SubscriptionService subscriptionService;

  @KafkaListener(topics = "${subscriptionTopicId}", groupId = "animalGroupId")
  public void listen(ConsumerRecord<String, String> record) throws IOException {

    AnimalNotificationUserSubscribedEvent animalNotificationUserSubscribedEvent =
        new ObjectMapper().readValue(record.value(), AnimalNotificationUserSubscribedEvent.class);

    subscriptionService.savePendingSubscriber(animalNotificationUserSubscribedEvent.getEmail(),
        animalNotificationUserSubscribedEvent.getApprover(),
        animalNotificationUserSubscribedEvent.getTopic());
  }
}
