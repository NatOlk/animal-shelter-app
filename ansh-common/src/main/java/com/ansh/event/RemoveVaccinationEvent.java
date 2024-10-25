package com.ansh.event;

import com.ansh.entity.Animal;
import com.ansh.entity.Vaccination;


public class RemoveVaccinationEvent extends VaccinationEvent {

  public RemoveVaccinationEvent(Animal animal, Vaccination vaccination) {
    super(animal, vaccination);
  }

  public RemoveVaccinationEvent() {
    super();
  }
}
