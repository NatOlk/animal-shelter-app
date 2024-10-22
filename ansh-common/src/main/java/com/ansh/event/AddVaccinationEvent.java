package com.ansh.event;

import com.ansh.entity.Animal;
import com.ansh.entity.Vaccination;

public class AddVaccinationEvent extends VaccinationEvent{

  public AddVaccinationEvent(Animal animal, Vaccination vaccination) {
    super(animal, vaccination);
  }
}
