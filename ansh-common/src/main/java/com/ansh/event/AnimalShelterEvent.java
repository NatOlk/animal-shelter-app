package com.ansh.event;

import static com.ansh.event.AnimalEventKeys.ANIMAL_DETAILS_LINK;
import static com.ansh.event.AnimalEventKeys.ANIMAL_GENDER;
import static com.ansh.event.AnimalEventKeys.ANIMAL_IMPLANT_CHIP;
import static com.ansh.event.AnimalEventKeys.ANIMAL_NAME;
import static com.ansh.event.AnimalEventKeys.ANIMAL_SPECIES;
import static com.ansh.event.AnimalEventKeys.BASE_URL_METADATA;
import static com.ansh.event.AnimalEventKeys.DATE_ADDED;

import com.ansh.entity.animal.Animal;
import com.ansh.event.animal.AddAnimalEvent;
import com.ansh.event.animal.RemoveAnimalEvent;
import com.ansh.event.vaccination.AddVaccinationEvent;
import com.ansh.event.vaccination.RemoveVaccinationEvent;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = AddAnimalEvent.class, name = "addAnimalEvent"),
    @JsonSubTypes.Type(value = RemoveAnimalEvent.class, name = "removeAnimalEvent"),
    @JsonSubTypes.Type(value = AddVaccinationEvent.class, name = "addVaccinationEvent"),
    @JsonSubTypes.Type(value = RemoveVaccinationEvent.class, name = "removeVaccinationEvent"),
    @JsonSubTypes.Type(value = RemoveVaccinationEvent.class, name = "animalShelterNewsEvent")
})
@Getter
@Setter
@NoArgsConstructor
public class AnimalShelterEvent {

  private Animal animal;

  private Map<String, Object> metadata = new HashMap<>();

  private LocalDate created = LocalDate.now();

  public AnimalShelterEvent(Animal animal) {
    this.animal = animal;
  }

  public Map<String, Object> getParams() {
    Map<String, Object> params = new HashMap<>();

    params.put(ANIMAL_NAME.getKey(), animal.getName());
    params.put(ANIMAL_SPECIES.getKey(), animal.getSpecies());
    params.put(ANIMAL_IMPLANT_CHIP.getKey(), animal.getImplantChipId());
    params.put(ANIMAL_GENDER.getKey(), animal.getGender());
    params.put(DATE_ADDED.getKey(), animal.getAdmissionDate());
    params.put(ANIMAL_DETAILS_LINK.getKey(),
        metadata.getOrDefault(BASE_URL_METADATA.getKey(), "")
            + "/public/animals/" + animal.getId());
    return params;
  }

  public void addMetadata(String key, Object value) {
    metadata.put(key, value);
  }
}
