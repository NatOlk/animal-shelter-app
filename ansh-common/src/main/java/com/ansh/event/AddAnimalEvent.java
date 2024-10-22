package com.ansh.event;

import com.ansh.entity.Animal;

public class AddAnimalEvent extends AnimalEvent {

  public AddAnimalEvent(Animal animal) {
    super(animal);
  }

}
