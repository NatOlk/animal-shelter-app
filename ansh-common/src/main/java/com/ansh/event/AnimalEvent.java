package com.ansh.event;

import com.ansh.entity.animal.Animal;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.beans.ConstructorProperties;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = AddAnimalEvent.class, name = "addAnimalEvent"),
    @JsonSubTypes.Type(value = RemoveAnimalEvent.class, name = "removeAnimalEvent"),
    @JsonSubTypes.Type(value = AddVaccinationEvent.class, name = "addVaccinationEvent"),
    @JsonSubTypes.Type(value = RemoveVaccinationEvent.class, name = "removeVaccinationEvent")
})
@Getter
@Setter
@NoArgsConstructor
public abstract class AnimalEvent {

  private Animal animal;

  private LocalDate created = LocalDate.now();

  AnimalEvent(Animal animal) {
    this.animal = animal;
  }

  public Map<String, Object> getParams() {
    Map<String, Object> params = new HashMap<>();

    params.put("animalName", animal.getName());
    params.put("animalSpecies", animal.getSpecies());
    params.put("animalImplantChip", animal.getImplantChipId());
    params.put("animalGender", animal.getGender());
    params.put("dateAdded", animal.getAdmissionDate());
    params.put("animalDetailsLink", "/details/" + animal.getId());

    return params;
  }
}
