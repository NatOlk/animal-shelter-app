package com.ansh.app.service.animal;

import com.ansh.app.service.exception.animal.AnimalCreationException;
import com.ansh.app.service.exception.animal.AnimalNotFoundException;
import com.ansh.app.service.exception.animal.AnimalUpdateException;
import com.ansh.entity.animal.Animal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.lang.NonNull;

/**
 * Service for managing animal records in the system.
 */
public interface AnimalService {

  /**
   * Retrieves a list of all animals.
   *
   * @return a list of {@link Animal} objects
   */
  List<Animal> getAllAnimals();

  /**
   * Finds an animal by its unique ID.
   *
   * @param id the unique ID of the animal
   * @return the found {@link Animal} object
   * @throws AnimalNotFoundException if the animal is not found
   */
  Animal findById(Long id) throws AnimalNotFoundException;

  /**
   * Adds a new animal to the system.
   *
   * @param name the name of the animal (required)
   * @param species the species of the animal (required)
   * @param primaryColor the primary color of the animal (required)
   * @param breed the breed of the animal (optional)
   * @param implantChipId the implant chip ID (optional)
   * @param gender the gender of the animal (required)
   * @param birthDate the birth date of the animal (optional)
   * @param pattern the fur pattern of the animal (optional)
   * @return the newly created {@link Animal} object
   * @throws AnimalCreationException if the animal cannot be created due to validation errors or system issues
   */
  Animal addAnimal(@NonNull String name, @NonNull String species,
      @NonNull String primaryColor, String breed,
      String implantChipId, @NonNull String gender,
      LocalDate birthDate, String pattern) throws AnimalCreationException;

  /**
   * Updates an existing animal's information.
   *
   * @param id the unique ID of the animal to update (required)
   * @param primaryColor the updated primary color (optional)
   * @param breed the updated breed (optional)
   * @param gender the updated gender (optional)
   * @param birthDate the updated birth date (optional)
   * @param pattern the updated fur pattern (optional)
   * @param photoImgPath the updated photo image path (optional)
   * @return the updated {@link Animal} object
   * @throws AnimalNotFoundException if the animal is not found
   * @throws AnimalUpdateException if the update fails due to validation errors or system issues
   */
  Animal updateAnimal(@NonNull Long id, String primaryColor,
      String breed, String gender,
      LocalDate birthDate, String pattern, String photoImgPath)
      throws AnimalNotFoundException, AnimalUpdateException;

  /**
   * Removes an animal from the system.
   *
   * @param id the unique ID of the animal to remove (required)
   * @param reason the reason for removal (optional)
   * @return the removed {@link Animal} object
   * @throws AnimalNotFoundException if the animal is not found
   */
  Animal removeAnimal(@NonNull Long id, String reason) throws AnimalNotFoundException;

  /**
   * Finds an animal by its vaccination ID.
   *
   * @param vaccinationId the vaccination ID associated with the animal
   * @return the found {@link Animal} object
   */
  Animal findAnimalByVaccinationId(Long vaccinationId);

  /**
   * Updates the photo URL of a specific animal.
   *
   * @param id the unique ID of the animal
   * @param path the new photo URL or file path
   */
  void updatePhotoUrl(Long id, String path);
}
