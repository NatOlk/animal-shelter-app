package com.ansh.event.vaccination;

import com.ansh.entity.animal.Animal;
import com.ansh.entity.animal.Vaccination;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class RemoveVaccinationEvent extends VaccinationEvent {

  public RemoveVaccinationEvent(Animal animal, Vaccination vaccination) {
    super(animal, vaccination);
  }

  public RemoveVaccinationEvent() {
    super();
  }
}
