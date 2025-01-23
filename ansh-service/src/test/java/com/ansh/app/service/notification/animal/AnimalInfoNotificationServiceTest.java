package com.ansh.app.service.notification.animal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.ansh.entity.animal.Animal;
import com.ansh.entity.animal.Vaccination;
import com.ansh.event.AddAnimalEvent;
import com.ansh.event.AddVaccinationEvent;
import com.ansh.event.RemoveAnimalEvent;
import com.ansh.event.RemoveVaccinationEvent;
import com.ansh.notification.app.animal.AnimalInfoNotificationProducer;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class AnimalInfoNotificationServiceTest {

  @Mock
  private AnimalInfoNotificationProducer animalInfoProducer;

  @InjectMocks
  private AnimalInfoNotificationService animalInfoNotificationService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
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

    verify(animalInfoProducer, times(1)).sendNotification(any(AddVaccinationEvent.class));
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

    verify(animalInfoProducer, times(1)).sendNotification(any(RemoveVaccinationEvent.class));
  }
}
