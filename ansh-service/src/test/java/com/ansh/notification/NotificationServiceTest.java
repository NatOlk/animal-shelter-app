package com.ansh.notification;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.ansh.entity.animal.Animal;
import com.ansh.entity.animal.Vaccination;
import com.ansh.event.AddAnimalEvent;
import com.ansh.event.AddVaccinationEvent;
import com.ansh.event.RemoveAnimalEvent;
import com.ansh.event.RemoveVaccinationEvent;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class NotificationServiceTest {

  @Mock
  private AnimalInfoNotificationProducer animalInfoProducer;

  @InjectMocks
  private NotificationService notificationService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testSendAddAnimalMessage() {
    Animal animal = new Animal();
    animal.setId(1L);
    animal.setName("Buddy");

    notificationService.sendAddAnimalMessage(animal);

    verify(animalInfoProducer, times(1)).sendMessage(any(AddAnimalEvent.class));
  }

  @Test
  void testSendRemoveAnimalMessage() {
    Animal animal = new Animal();
    animal.setId(2L);
    animal.setName("Max");
    String reason = "Adopted";

    notificationService.sendRemoveAnimalMessage(animal, reason);

    verify(animalInfoProducer, times(1)).sendMessage(any(RemoveAnimalEvent.class));
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

    notificationService.sendAddVaccinationMessage(vaccination);

    verify(animalInfoProducer, times(1)).sendMessage(any(AddVaccinationEvent.class));
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

    notificationService.sendRemoveVaccinationMessage(vaccination);

    verify(animalInfoProducer, times(1)).sendMessage(any(RemoveVaccinationEvent.class));
  }
}
