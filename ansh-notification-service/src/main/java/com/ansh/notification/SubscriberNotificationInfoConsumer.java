package com.ansh.notification;

import com.ansh.event.subscription.AnimalNotificationUserSubscribedEvent;
import com.ansh.service.AnimalTopicSubscriberRegistryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class SubscriberNotificationInfoConsumer {

  @Value("${approveTopicId}")
  private String approveTopicId;

  @Autowired
  private AnimalTopicSubscriberRegistryService topicSubscriberRegistryService;

  @KafkaListener(topics = "${approveTopicId}", groupId = "animalGroupId")
  public void listen(ConsumerRecord<String, String> record) throws IOException {

    AnimalNotificationUserSubscribedEvent animalEvent = new ObjectMapper().readValue(record.value(),
        AnimalNotificationUserSubscribedEvent.class);
    String email = animalEvent.getEmail();
    String approver = animalEvent.getApprover();
    String topic = animalEvent.getTopic();
    topicSubscriberRegistryService.approveSubscriber(email, approver, topic);
  }
}
