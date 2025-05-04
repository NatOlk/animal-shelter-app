package com.ansh.event;

public enum AnimalShelterTopic {
  ANIMAL_INFO("animalTopicId"),
  VACCINATION_INFO("vaccinationTopicId"),
  ANIMAL_SHELTER_NEWS("animalShelterNewsTopicId");

  private final String topicName;

  AnimalShelterTopic(String topicName) {
    this.topicName = topicName;
  }

  public String getTopicName() {
    return topicName;
  }
}
