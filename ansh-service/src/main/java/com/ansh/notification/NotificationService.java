package com.ansh.notification;

import com.ansh.entity.Animal;
import com.ansh.entity.Vaccination;
import com.ansh.event.AddAnimalEvent;
import com.ansh.event.AddVaccinationEvent;
import com.ansh.event.AnimalEvent;
import com.ansh.event.RemoveAnimalEvent;
import com.ansh.event.RemoveVaccinationEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

  @Autowired
  private AnimalInfoProducer animalInfoProducer;

  @Value("${animalGroupTopicId}")
  private String animalGroupTopicId;

  public void sendAddAnimalMessage(Animal animal) {
    AnimalEvent addAnimalEvent = new AddAnimalEvent(animal);

    animalInfoProducer.sendMessage(animalGroupTopicId, addAnimalEvent);
  }

  public void sendRemoveAnimalMessage(Animal animal, String reason) {
    AnimalEvent removeAnimalEvent = new RemoveAnimalEvent(animal, reason);

    animalInfoProducer.sendMessage(animalGroupTopicId, removeAnimalEvent);
  }

  public void sendAddVaccinationMessage(Vaccination vaccination) {
    AnimalEvent addVaccinationEvent = new AddVaccinationEvent(vaccination.getAnimal(), vaccination);
    animalInfoProducer.sendMessage(animalGroupTopicId, addVaccinationEvent);
  }

  public void sendRemoveVaccinationMessage(Vaccination vaccination) {
    AnimalEvent addVaccinationEvent = new RemoveVaccinationEvent(vaccination.getAnimal(),
        vaccination);
    animalInfoProducer.sendMessage(animalGroupTopicId, addVaccinationEvent);
  }
}
