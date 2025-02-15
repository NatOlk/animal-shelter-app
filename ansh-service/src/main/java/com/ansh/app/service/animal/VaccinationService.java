package com.ansh.app.service.animal;

import com.ansh.app.service.exception.animal.VaccinationCreationException;
import com.ansh.app.service.exception.animal.VaccinationNotFoundException;
import com.ansh.app.service.exception.animal.VaccinationUpdateException;
import com.ansh.dto.UpdateVaccinationInput;
import com.ansh.dto.VaccinationInput;
import com.ansh.entity.animal.Vaccination;
import java.time.LocalDate;
import java.util.List;
import org.springframework.lang.NonNull;

/**
 * Service for managing animal vaccinations.
 */
public interface VaccinationService {

  /**
   * Retrieves a list of all vaccinations.
   *
   * @return a list of {@link Vaccination} objects
   */
  List<Vaccination> getAllVaccinations();

  /**
   * Finds all vaccinations for a specific animal by its ID.
   *
   * @param animalId the unique ID of the animal
   * @return a list of {@link Vaccination} records associated with the animal
   */
  List<Vaccination> findByAnimalId(Long animalId);

  /**
   * Counts the number of vaccinations for a specific vaccination ID.
   *
   * @param id the vaccination ID
   * @return the number of vaccinations recorded with this ID
   */
  int vaccinationCountById(Long id);

  /**
   * Adds a new vaccination record for an animal.
   *
   * @param vaccination an {@link VaccinationInput} object containing vaccination details
   * @return the created {@link Vaccination} object with a generated ID
   * @throws VaccinationCreationException if there is an error during the vaccination creation process
   */
  Vaccination addVaccination(@NonNull VaccinationInput vaccination) throws VaccinationCreationException;

  /**
   * Updates an existing vaccination record.
   *
   * @param vaccination an {@link UpdateVaccinationInput} object containing updated vaccination details
   * @return the updated {@link Vaccination} object
   * @throws VaccinationNotFoundException if the vaccination record with the given ID is not found
   * @throws VaccinationUpdateException if there is an error during the update process
   */
  Vaccination updateVaccination(@NonNull UpdateVaccinationInput vaccination)
      throws VaccinationNotFoundException, VaccinationUpdateException;

  /**
   * Deletes a vaccination record.
   *
   * @param id the unique ID of the vaccination to delete (required)
   * @return the deleted {@link Vaccination} object
   * @throws VaccinationNotFoundException if the vaccination record is not found
   */
  Vaccination deleteVaccination(@NonNull Long id) throws VaccinationNotFoundException;
}
