package com.ansh.event;

import com.ansh.entity.animal.Animal;
import com.ansh.entity.animal.Vaccination;


public class RemoveVaccinationEvent extends VaccinationEvent {

  public RemoveVaccinationEvent(Animal animal, Vaccination vaccination) {
    super(animal, vaccination);
  }

  public RemoveVaccinationEvent() {
    super();
  }
}
