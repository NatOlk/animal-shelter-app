package com.ansh.event;

import lombok.Getter;

@Getter
public enum AnimalEventKeys {
  BASE_URL_METADATA("BASE_URL"),
  ANIMAL_NAME("animalName"),
  ANIMAL_SPECIES("animalSpecies"),
  ANIMAL_IMPLANT_CHIP("animalImplantChip"),
  ANIMAL_GENDER("animalGender"),
  DATE_ADDED("dateAdded"),
  ANIMAL_DETAILS_LINK("animalDetailsLink");

  private final String key;

  AnimalEventKeys(String key) {
    this.key = key;
  }
}
