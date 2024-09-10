package com.example.gr.service;

import com.example.gr.jpa.data.Animal;
import com.example.gr.jpa.data.Vaccination;
import com.example.gr.kafka.AnimalInfoProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.gr.kafka.SubscriptionMessages.ADD_ANIMAL_MSG;
import static com.example.gr.kafka.SubscriptionMessages.ADD_VACCINE_MSG;

@Service
public class NotificationService {

    private static final String ADD_ANIMAL_TEMPLATE = "addAnimalTemplate";

    private static final String ADD_VACCINE_TEMPLATE = "addVaccineTemplate";

    @Autowired
    private AnimalInfoProducer animalInfoProducer;

    public void sendAddAnimalMessage(Animal animal) throws JsonProcessingException {

        Map<String, Object> params = new HashMap<>(2);
        params.put("animalName", animal.getName());
        params.put("animalSpecies", animal.getSpecies());
        params.put("animalImplantChip", animal.getImplant_chip_id());
        params.put("animalGender", animal.getGender());
        params.put("dateAdded", new Date());

        animalInfoProducer.sendMessage(String.format(ADD_ANIMAL_MSG,
                        animal.getName(), animal.getSpecies()),
                ADD_ANIMAL_TEMPLATE, params);


    }
    public void sendAddVaccinationMessage(Vaccination vaccination) throws JsonProcessingException {

        Map<String, Object> params = new HashMap<>(2);
        params.put("animalName", vaccination.getAnimal().getName());
        params.put("vaccineName", vaccination.getVaccine());
        params.put("vaccineDate", vaccination.getVaccination_time());

        animalInfoProducer.sendMessage(
                String.format(ADD_VACCINE_MSG, vaccination.getAnimal().getName(),
                vaccination.getAnimal().getSpecies(), vaccination.getVaccine(),
                        vaccination.getBatch(), vaccination.getVaccination_time()),
                ADD_VACCINE_TEMPLATE, params);
    }

    public void sendAdoptAnimalMessage() {

    }
}
