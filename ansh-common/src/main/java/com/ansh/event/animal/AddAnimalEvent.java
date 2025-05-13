package com.ansh.event.animal;

import com.ansh.entity.animal.Animal;
import com.ansh.event.AnimalShelterEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AddAnimalEvent extends AnimalShelterEvent {

  public AddAnimalEvent(Animal animal) {
    super(animal);
  }

  public AddAnimalEvent() {
    super();
  }
}
