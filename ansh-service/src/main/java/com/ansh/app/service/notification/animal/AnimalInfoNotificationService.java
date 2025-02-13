package com.ansh.app.service.notification.animal;

import com.ansh.entity.animal.Animal;
import com.ansh.entity.animal.Vaccination;

/**
 * Service for sending notifications related to animal and vaccination events.
 */
public interface AnimalInfoNotificationService {

  /**
   * Sends a notification when a new animal is added.
   *
   * @param animal the {@link Animal} object that has been added
   */
  void sendAddAnimalMessage(Animal animal);

  /**
   * Sends a notification when an animal is removed.
   *
   * @param animal the {@link Animal} object that has been removed
   * @param reason the reason for removing the animal
   */
  void sendRemoveAnimalMessage(Animal animal, String reason);

  /**
   * Sends a notification when a new vaccination record is added.
   *
   * @param vaccination the {@link Vaccination} object that has been added
   */
  void sendAddVaccinationMessage(Vaccination vaccination);

  /**
   * Sends a notification when a vaccination record is removed.
   *
   * @param vaccination the {@link Vaccination} object that has been removed
   */
  void sendRemoveVaccinationMessage(Vaccination vaccination);
}
