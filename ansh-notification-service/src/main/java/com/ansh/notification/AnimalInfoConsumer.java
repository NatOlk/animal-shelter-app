package com.ansh.notification;

import com.ansh.event.AnimalEvent;
import com.ansh.notification.handler.AnimalEventHandlerRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class AnimalInfoConsumer {
  @Value("${animalGroupTopicId}")
  private String animalGroupTopicId;

  @Autowired
  private AnimalEventHandlerRegistry handlerRegistry;

  @KafkaListener(topics = "${animalGroupTopicId}", groupId = "animalGroupId")
  public void listen(ConsumerRecord<String, String> record) throws IOException {

    AnimalEvent animalEvent = new ObjectMapper().readValue(record.value(), AnimalEvent.class);
    handlerRegistry.handleEvent(animalEvent);
  }
}