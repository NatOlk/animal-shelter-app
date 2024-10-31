package com.ansh.event;

import com.ansh.entity.animal.Animal;
import com.ansh.entity.animal.Vaccination;

public class AddVaccinationEvent extends VaccinationEvent {

  public AddVaccinationEvent(Animal animal, Vaccination vaccination) {
    super(animal, vaccination);
  }

  public AddVaccinationEvent() {
    super();
  }
}
