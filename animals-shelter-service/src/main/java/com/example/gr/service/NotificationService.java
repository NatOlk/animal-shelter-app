package com.example.gr.service;

import com.example.gr.jpa.data.Animal;
import com.example.gr.jpa.data.Vaccination;
import com.example.gr.kafka.AnimalInfoProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class NotificationService {

    @Autowired
    private AnimalInfoProducer animalInfoProducer;

    @Value("${animalGroupTopicId}")
    private String animalGroupTopicId;

    public void sendAddAnimalMessage(Animal animal) throws JsonProcessingException {
        Map<String, Object> message = new HashMap<>();
        message.put("eventType", "addAnimal");
        Map<String, Object> params = new HashMap<>();

        params.put("animalName", animal.getName());
        params.put("animalSpecies", animal.getSpecies());
        params.put("animalImplantChip", animal.getImplantChipId());
        params.put("animalGender", animal.getGender());
        params.put("dateAdded", animal.getAdmissionDate());

        message.put("data", params);

        animalInfoProducer.sendMessage(animalGroupTopicId, message);
    }

    public void sendRemoveAnimalMessage(String name, String species) throws JsonProcessingException {
        Map<String, Object> message = new HashMap<>();
        message.put("eventType", "removeAnimal");
        Map<String, Object> params = new HashMap<>();

        params.put("animalName", name);
        params.put("animalSpecies", species);
        message.put("data", params);

        animalInfoProducer.sendMessage(animalGroupTopicId, message);
    }

    public void sendAddVaccinationMessage(Vaccination vaccination) throws JsonProcessingException {
        Map<String, Object> message = new HashMap<>();
        message.put("eventType", "addVaccine");
        Map<String, Object> params = new HashMap<>();

        params.put("animalName", vaccination.getAnimal().getName());
        params.put("animalSpecies", vaccination.getAnimal().getSpecies());
        params.put("vaccineName", vaccination.getVaccine());
        params.put("vaccineDate", vaccination.getVaccinationTime());

        message.put("data", params);

        animalInfoProducer.sendMessage(animalGroupTopicId, message);
    }

    public void sendAdoptAnimalMessage(String name) throws JsonProcessingException {
        Map<String, Object> message = new HashMap<>();
        Map<String, Object> params = new HashMap<>();

        params.put("name", name);
        message.put("data", params);

        animalInfoProducer.sendMessage(animalGroupTopicId, message);
    }
}
