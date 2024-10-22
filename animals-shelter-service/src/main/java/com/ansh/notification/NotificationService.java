package com.ansh.notification;

import com.ansh.entity.Animal;
import com.ansh.entity.Vaccination;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
    Map<String, Object> message = new HashMap<>();
    message.put("eventType", "addAnimal");
    Map<String, Object> params = new HashMap<>();

    addAnimalInformation(animal, params);
    message.put("data", params);

    animalInfoProducer.sendMessage(animalGroupTopicId, message);
  }

  public void sendRemoveAnimalMessage(Animal animal, String reason) {
    Map<String, Object> message = new HashMap<>();
    message.put("eventType", "removeAnimal");
    Map<String, Object> params = new HashMap<>();

    addAnimalInformation(animal, params);

    params.put("reason", reason);
    params.put("dateRemoved", new Date());

    message.put("data", params);

    animalInfoProducer.sendMessage(animalGroupTopicId, message);
  }

  public void sendAddVaccinationMessage(Vaccination vaccination) {
    Map<String, Object> message = new HashMap<>();
    message.put("eventType", "addVaccine");
    Map<String, Object> params = new HashMap<>();

    addAnimalInformation(vaccination.getAnimal(), params);
    addVaccinationInformation(vaccination, params);

    message.put("data", params);

    animalInfoProducer.sendMessage(animalGroupTopicId, message);
  }

  public void sendAdoptAnimalMessage(String name) {
    Map<String, Object> message = new HashMap<>();
    Map<String, Object> params = new HashMap<>();

    params.put("name", name);
    message.put("data", params);

    animalInfoProducer.sendMessage(animalGroupTopicId, message);
  }

  private void addAnimalInformation(Animal animal, Map<String, Object> params) {
    params.put("animalName", animal.getName());
    params.put("animalSpecies", animal.getSpecies());
    params.put("animalImplantChip", animal.getImplantChipId());
    params.put("animalGender", animal.getGender());
    params.put("dateAdded", animal.getAdmissionDate());
  }

  private void addVaccinationInformation(Vaccination vaccination, Map<String, Object> params) {
    params.put("vaccineName", vaccination.getVaccine());
    params.put("vaccineDate", vaccination.getVaccinationTime());
  }
}
