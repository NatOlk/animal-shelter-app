package com.ansh.event.vaccination;

import com.ansh.entity.animal.Animal;
import com.ansh.entity.animal.Vaccination;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AddVaccinationEvent extends VaccinationEvent {

  public AddVaccinationEvent(Animal animal, Vaccination vaccination) {
    super(animal, vaccination);
  }

  public AddVaccinationEvent() {
    super();
  }
}
