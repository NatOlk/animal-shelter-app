package com.ansh.notification.app.animal;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ansh.event.animal.AddAnimalEvent;
import com.ansh.event.AnimalShelterEvent;
import com.ansh.event.AnimalShelterTopic;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

class AnimalInfoNotificationProducerTest {


  @Mock
  private KafkaTemplate<String, String> kafkaTemplate;

  @Mock
  private ObjectMapper objectMapper;

  @InjectMocks
  private AnimalInfoNotificationProducer animalInfoNotificationProducer;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void sendMessage_success() throws JsonProcessingException {
    AnimalShelterEvent animalShelterEvent = new AddAnimalEvent();
    String jsonMessage = "{\"event\":\"test\"}";

    when(objectMapper.writeValueAsString(animalShelterEvent)).thenReturn(jsonMessage);

    animalInfoNotificationProducer.sendNotification(animalShelterEvent);

    verify(kafkaTemplate, times(1))
        .send(eq(AnimalShelterTopic.ANIMAL_INFO.getTopicName()), eq(jsonMessage));
  }

  @Test
  void sendMessage_jsonProcessingException() throws JsonProcessingException {
    AnimalShelterEvent animalShelterEvent = new AddAnimalEvent();

    when(objectMapper.writeValueAsString(animalShelterEvent)).thenThrow(
        new JsonProcessingException("Error") {
        });

    animalInfoNotificationProducer.sendNotification(animalShelterEvent);

    verify(kafkaTemplate, never()).send(anyString(), anyString());
  }
}
