package com.ansh.notification;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ansh.event.AddAnimalEvent;
import com.ansh.event.AnimalEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

class AnimalInfoNotificationProducerTest {

  private static final String ANIMAL_TOPIC = "animalTopic";

  @Mock
  private KafkaTemplate<String, String> kafkaTemplate;

  @Mock
  private ObjectMapper objectMapper;

  @InjectMocks
  private AnimalInfoNotificationProducer animalInfoNotificationProducer;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    animalInfoNotificationProducer.setAnimalTopicId(ANIMAL_TOPIC);
  }

  @Test
  void sendMessage_success() throws JsonProcessingException {
    AnimalEvent animalEvent = new AddAnimalEvent();
    String jsonMessage = "{\"event\":\"test\"}";

    when(objectMapper.writeValueAsString(animalEvent)).thenReturn(jsonMessage);

    animalInfoNotificationProducer.sendMessage(animalEvent);

    verify(kafkaTemplate, times(1)).send(eq(ANIMAL_TOPIC), eq(jsonMessage));
  }

  @Test
  void sendMessage_jsonProcessingException() throws JsonProcessingException {
    AnimalEvent animalEvent = new AddAnimalEvent();

    when(objectMapper.writeValueAsString(animalEvent)).thenThrow(
        new JsonProcessingException("Error") {
        });

    animalInfoNotificationProducer.sendMessage(animalEvent);

    verify(kafkaTemplate, never()).send(anyString(), anyString());
  }
}
