package com.ansh.event.vaccination;

import com.ansh.entity.animal.Animal;
import com.ansh.entity.animal.Vaccination;
import com.ansh.event.AnimalShelterEvent;
import com.ansh.event.vaccination.AddVaccinationEvent;
import com.ansh.event.vaccination.RemoveVaccinationEvent;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = AddVaccinationEvent.class, name = "addVaccinationEvent"),
    @JsonSubTypes.Type(value = RemoveVaccinationEvent.class, name = "removeVaccinationEvent")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class VaccinationEvent extends AnimalShelterEvent {

  @JsonProperty("vaccination")
  private Vaccination vaccination;

  protected VaccinationEvent(Animal animal, Vaccination vaccination) {
    super(animal);
    this.vaccination = vaccination;
  }

  @Override
  public Map<String, Object> getParams() {
    Map<String, Object> params = new HashMap<>();

    if (getAnimal() != null) {
      params.put("animalName", getAnimal().getName());
      params.put("animalSpecies", getAnimal().getSpecies());
      params.put("animalImplantChip", getAnimal().getImplantChipId());
      params.put("animalGender", getAnimal().getGender());
      params.put("dateAdded", getAnimal().getAdmissionDate());
    }
    if (vaccination != null) {
      params.put("vaccineName", vaccination.getVaccine());
      params.put("vaccineDate", vaccination.getVaccinationTime());
      params.put("vaccineBatch", vaccination.getBatch());
    }
    return params;
  }
}
