package com.ansh.notification.app;

import static com.ansh.event.AnimalShelterTopic.ANIMAL_INFO;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.ansh.event.AnimalShelterEvent;
import com.ansh.event.AnimalShelterTopic;
import com.ansh.event.animal.AddAnimalEvent;
import com.ansh.notification.app.handler.AnimalNotificationHandlerRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


class AnimalShelterEventConsumerTest {

  @Mock
  private AnimalNotificationHandlerRegistry handlerRegistry;

  @InjectMocks
  private AnimalEventConsumer animalEventConsumer;

  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();
  }

  @Test
  void testListen() throws IOException {

    String json = "{\"type\":\"addAnimalEvent\", \"animal\":{\"name\":\"Rex\", \"species\":\"Dog\","
        + " \"implantChipId\":\"1234567890\", \"gender\":\"M\", \"admissionDate\":\"2024-12-01\"}}";
    ConsumerRecord<String, String> message = new ConsumerRecord<>(ANIMAL_INFO.getTopicName(), 0, 0L, "key", json);

    AnimalShelterEvent expectedEvent = objectMapper.readValue(json, AddAnimalEvent.class);

    animalEventConsumer.listenAnimalTopic(message);

    ArgumentCaptor<AnimalShelterEvent> captor = ArgumentCaptor.forClass(AnimalShelterEvent.class);
    verify(handlerRegistry, times(1))
        .handleEvent(eq(ANIMAL_INFO.getTopicName()), captor.capture());

    AnimalShelterEvent capturedEvent = captor.getValue();
    assert capturedEvent instanceof AddAnimalEvent : "Captured event is not of type AddAnimalEvent";
    assert capturedEvent.getAnimal().getName()
        .equals(expectedEvent.getAnimal().getName()) : "Animal name doesn't match";
    assert capturedEvent.getAnimal().getSpecies()
        .equals(expectedEvent.getAnimal().getSpecies()) : "Animal species doesn't match";
    assert capturedEvent.getAnimal().getImplantChipId()
        .equals(expectedEvent.getAnimal().getImplantChipId()) : "Implant chip ID doesn't match";
    assert capturedEvent.getAnimal().getGender() == expectedEvent.getAnimal()
        .getGender() : "Gender doesn't match";
    assert capturedEvent.getAnimal().getAdmissionDate()
        .equals(expectedEvent.getAnimal().getAdmissionDate()) : "Admission date doesn't match";

  }
}
