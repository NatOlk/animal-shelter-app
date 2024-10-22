package com.ansh.event;

import com.ansh.entity.Animal;
import com.ansh.entity.Vaccination;
import java.util.HashMap;
import java.util.Map;

public abstract class VaccinationEvent extends AnimalEvent{

  private final Vaccination vaccination;

  protected VaccinationEvent(Animal animal, Vaccination vaccination) {
    super(animal);
    this.vaccination = vaccination;
  }

  @Override
  public Map<String, Object> getParams() {
    Map<String, Object> params = new HashMap<String, Object>();

    params.put("animalName", vaccination.getAnimal().getName());
    params.put("animalSpecies", vaccination.getAnimal().getSpecies());
    params.put("animalImplantChip", vaccination.getAnimal().getImplantChipId());
    params.put("animalGender", vaccination.getAnimal().getGender());
    params.put("dateAdded", vaccination.getAnimal().getAdmissionDate());
    params.put("vaccineName", vaccination.getVaccine());
    params.put("vaccineDate", vaccination.getVaccinationTime());
    params.put("vaccineBatch", vaccination.getBatch());

    return params;
  }
}
