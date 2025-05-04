package com.ansh.notification.app;

import com.ansh.event.AnimalShelterEvent;
import com.ansh.event.AnimalShelterTopic;
import com.ansh.notification.app.handler.AnimalNotificationHandlerRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class AnimalEventConsumer {

  @Autowired
  private AnimalNotificationHandlerRegistry handlerRegistry;

  @KafkaListener(topics = {"${animalTopicId}"}, groupId = "notificationGroupId")
  public void listenAnimalTopic(ConsumerRecord<String, String> message) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();
    AnimalShelterEvent animalShelterEvent = objectMapper.readValue(message.value(),
        AnimalShelterEvent.class);
    handlerRegistry.handleEvent(AnimalShelterTopic.ANIMAL_INFO.getTopicName(), animalShelterEvent);
  }

  @KafkaListener(topics = {"${vaccinationTopicId}"}, groupId = "notificationGroupId")
  public void listenVaccinationTopic(ConsumerRecord<String, String> message) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();
    AnimalShelterEvent animalShelterEvent = objectMapper.readValue(message.value(),
        AnimalShelterEvent.class);
    handlerRegistry.handleEvent(AnimalShelterTopic.VACCINATION_INFO.getTopicName(),
        animalShelterEvent);
  }

  @KafkaListener(topics = {"${animalShelterNewsTopicId}"}, groupId = "notificationGroupId")
  public void listenAnimalNewsTopic(ConsumerRecord<String, String> message) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();
    AnimalShelterEvent animalShelterEvent = objectMapper.readValue(message.value(),
        AnimalShelterEvent.class);
    handlerRegistry.handleEvent(AnimalShelterTopic.ANIMAL_SHELTER_NEWS.getTopicName(),
        animalShelterEvent);
  }
}
