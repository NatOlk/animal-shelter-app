package com.ansh.app.service.notification.animal.impl;

import com.ansh.app.service.notification.animal.AnimalInfoNotificationService;
import com.ansh.entity.animal.Animal;
import com.ansh.entity.animal.Vaccination;
import com.ansh.event.AddAnimalEvent;
import com.ansh.event.AddVaccinationEvent;
import com.ansh.event.AnimalEvent;
import com.ansh.event.RemoveAnimalEvent;
import com.ansh.event.RemoveVaccinationEvent;
import com.ansh.notification.app.animal.AnimalInfoNotificationProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnimalInfoNotificationServiceImpl implements AnimalInfoNotificationService {

  @Autowired
  private AnimalInfoNotificationProducer animalInfoProducer;

  @Override
  public void sendAddAnimalMessage(Animal animal) {
    AnimalEvent addAnimalEvent = new AddAnimalEvent(animal);
    animalInfoProducer.sendNotification(addAnimalEvent);
  }

  @Override
  public void sendRemoveAnimalMessage(Animal animal, String reason) {
    AnimalEvent removeAnimalEvent = new RemoveAnimalEvent(animal, reason);
    animalInfoProducer.sendNotification(removeAnimalEvent);
  }

  @Override
  public void sendAddVaccinationMessage(Vaccination vaccination) {
    AnimalEvent addVaccinationEvent = new AddVaccinationEvent(vaccination.getAnimal(), vaccination);
    animalInfoProducer.sendNotification(addVaccinationEvent);
  }

  @Override
  public void sendRemoveVaccinationMessage(Vaccination vaccination) {
    AnimalEvent addVaccinationEvent = new RemoveVaccinationEvent(vaccination.getAnimal(),
        vaccination);
    animalInfoProducer.sendNotification(addVaccinationEvent);
  }
}
