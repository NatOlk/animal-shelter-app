package com.ansh.notification;

import com.ansh.event.subscription.AnimalNotificationUserSubscribedEvent;
import com.ansh.notification.handler.AnimalEventHandlerRegistry;
import com.ansh.service.TopicSubscriberRegistryService;
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
  private TopicSubscriberRegistryService topicSubscriberRegistryService;

  @KafkaListener(topics = "${approveTopicId}", groupId = "animalGroupId")
  public void listen(ConsumerRecord<String, String> record) throws IOException {

    AnimalNotificationUserSubscribedEvent animalEvent = new ObjectMapper().readValue(record.value(),
        AnimalNotificationUserSubscribedEvent.class);
    String email = animalEvent.getEmail();
    topicSubscriberRegistryService.approveSubscriber(email);
  }
}
