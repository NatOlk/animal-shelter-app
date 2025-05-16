package com.ansh.app.service.notification.animal.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ansh.entity.animal.Animal;
import com.ansh.entity.animal.Vaccination;
import com.ansh.event.AnimalShelterTopic;
import com.ansh.event.animal.AddAnimalEvent;
import com.ansh.event.animal.RemoveAnimalEvent;
import com.ansh.event.vaccination.AddVaccinationEvent;
import com.ansh.event.vaccination.RemoveVaccinationEvent;
import com.ansh.notification.app.animal.AnimalInfoNotificationProducer;
import com.ansh.notification.app.vaccination.VaccinationInfoNotificationProducer;
import com.ansh.notification.strategy.NotificationProducerStrategy;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class AnimalInfoNotificationServiceTest {

  @Mock
  private AnimalInfoNotificationProducer animalInfoProducer;

  @Mock
  private VaccinationInfoNotificationProducer vaccinationInfoNotificationProducer;

  @Mock
  private NotificationProducerStrategy notificationProducerStrategy;

  @InjectMocks
  private AnimalInfoNotificationServiceImpl animalInfoNotificationService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    when(notificationProducerStrategy.getServiceByTopic(
        AnimalShelterTopic.ANIMAL_INFO.getTopicName()))
        .thenReturn(Optional.of(animalInfoProducer));
    when(notificationProducerStrategy.getServiceByTopic(
        AnimalShelterTopic.VACCINATION_INFO.getTopicName()))
        .thenReturn(Optional.of(vaccinationInfoNotificationProducer));
  }

  @Test
  void testSendAddAnimalMessage() {
    Animal animal = new Animal();
    animal.setId(1L);
    animal.setName("Buddy");

    animalInfoNotificationService.sendAddAnimalMessage(animal);

    verify(animalInfoProducer, times(1)).sendNotification(any(AddAnimalEvent.class));
  }

  @Test
  void testSendRemoveAnimalMessage() {
    Animal animal = new Animal();
    animal.setId(2L);
    animal.setName("Max");
    String reason = "Adopted";

    animalInfoNotificationService.sendRemoveAnimalMessage(animal, reason);

    verify(animalInfoProducer, times(1)).sendNotification(any(RemoveAnimalEvent.class));
  }

  @Test
  void testSendAddVaccinationMessage() {
    Animal animal = new Animal();
    animal.setId(3L);
    animal.setName("Bella");
    Vaccination vaccination = new Vaccination();
    vaccination.setAnimal(animal);
    vaccination.setVaccine("Rabies");
    vaccination.setVaccinationTime(LocalDate.now());

    animalInfoNotificationService.sendAddVaccinationMessage(vaccination);

    verify(vaccinationInfoNotificationProducer, times(1)).sendNotification(
        any(AddVaccinationEvent.class));
  }

  @Test
  void testSendRemoveVaccinationMessage() {
    Animal animal = new Animal();
    animal.setId(4L);
    animal.setName("Charlie");
    Vaccination vaccination = new Vaccination();
    vaccination.setAnimal(animal);
    vaccination.setVaccine("Parvovirus");
    vaccination.setVaccinationTime(LocalDate.now());

    animalInfoNotificationService.sendRemoveVaccinationMessage(vaccination);

    verify(vaccinationInfoNotificationProducer, times(1)).sendNotification(
        any(RemoveVaccinationEvent.class));
  }
}
