package com.ansh.app.service.animal;

import com.ansh.app.service.exception.animal.AnimalCreationException;
import com.ansh.app.service.exception.animal.AnimalNotFoundException;
import com.ansh.app.service.exception.animal.AnimalUpdateException;
import com.ansh.dto.AnimalInput;
import com.ansh.dto.UpdateAnimalInput;
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
   * @param animalInput an {@link AnimalInput} object containing the details of the new animal
   * @return the created {@link Animal} object with generated ID and admission date
   * @throws AnimalCreationException if there is an error during animal creation
   */
  Animal addAnimal(@NonNull AnimalInput animalInput) throws AnimalCreationException;

  /**
   * Updates an existing animal's information.
   *
   * @param updateAnimalInput an {@link UpdateAnimalInput} object containing updated details
   * @return the updated {@link Animal} object
   * @throws AnimalNotFoundException if the animal with the given ID is not found
   * @throws AnimalUpdateException if there is an error during the update process
   */
  Animal updateAnimal(@NonNull UpdateAnimalInput updateAnimalInput)
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
