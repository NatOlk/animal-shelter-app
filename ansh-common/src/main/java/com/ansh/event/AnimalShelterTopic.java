package com.ansh.event;

import lombok.Getter;

@Getter
public enum AnimalShelterTopic {
  ANIMAL_INFO("animalTopicId"),
  VACCINATION_INFO("vaccinationTopicId"),
  ANIMAL_SHELTER_NEWS("animalShelterNewsTopicId");

  private final String topicName;

  AnimalShelterTopic(String topicName) {
    this.topicName = topicName;
  }
}
