package com.ansh.dto;

import com.ansh.entity.account.UserProfile.AnimalInfoNotifStatus;

public record NotificationStatusDTO(
    AnimalInfoNotifStatus animalShelterNewsTopicId,
    AnimalInfoNotifStatus animalTopicId,
    AnimalInfoNotifStatus vaccinationTopicId
) {
  public NotificationStatusDTO() {
    this(AnimalInfoNotifStatus.NONE, AnimalInfoNotifStatus.NONE, AnimalInfoNotifStatus.NONE);
  }
}

