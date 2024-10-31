package com.ansh.event;

import com.ansh.entity.animal.Animal;

public class AddAnimalEvent extends AnimalEvent {

  public AddAnimalEvent(Animal animal) {
    super(animal);
  }

  public AddAnimalEvent() {
    super();
  }
}
