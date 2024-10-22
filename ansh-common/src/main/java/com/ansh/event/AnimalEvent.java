package com.ansh.event;

import com.ansh.entity.Animal;
import java.util.HashMap;
import java.util.Map;

public abstract class AnimalEvent {

  private final Animal animal;

  protected AnimalEvent(Animal animal) {
    this.animal = animal;
  }

  public Map<String, Object> getParams() {
    Map<String, Object> params = new HashMap<String, Object>();

    params.put("animalName", animal.getName());
    params.put("animalSpecies", animal.getSpecies());
    params.put("animalImplantChip", animal.getImplantChipId());
    params.put("animalGender", animal.getGender());
    params.put("dateAdded", animal.getAdmissionDate());

    return params;
  }
}
