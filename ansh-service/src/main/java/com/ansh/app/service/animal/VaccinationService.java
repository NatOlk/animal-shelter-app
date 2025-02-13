package com.ansh.app.service.animal;

import com.ansh.app.service.exception.animal.VaccinationCreationException;
import com.ansh.app.service.exception.animal.VaccinationNotFoundException;
import com.ansh.app.service.exception.animal.VaccinationUpdateException;
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
   * Adds a new vaccination record.
   *
   * @param animalId the unique ID of the animal receiving the vaccination (required)
   * @param vaccine the name of the vaccine (required)
   * @param batch the batch number of the vaccine (required)
   * @param vaccinationTime the date of the vaccination (required)
   * @param comments additional comments regarding the vaccination (optional)
   * @param email the email of the responsible person or veterinarian (required)
   * @return the newly created {@link Vaccination} object
   * @throws VaccinationCreationException if the vaccination record cannot be created due to validation errors or system issues
   */
  Vaccination addVaccination(@NonNull Long animalId, @NonNull String vaccine,
      @NonNull String batch, @NonNull LocalDate vaccinationTime,
      String comments, @NonNull String email) throws VaccinationCreationException;

  /**
   * Updates an existing vaccination record.
   *
   * @param id the unique ID of the vaccination to update (required)
   * @param vaccine the updated vaccine name (optional)
   * @param batch the updated batch number (optional)
   * @param vaccinationTime the updated vaccination date (optional)
   * @param comments updated comments (optional)
   * @param email the updated email of the responsible person (optional)
   * @return the updated {@link Vaccination} object
   * @throws VaccinationNotFoundException if the vaccination record is not found
   * @throws VaccinationUpdateException if the update fails due to validation errors or system issues
   */
  Vaccination updateVaccination(@NonNull Long id, String vaccine,
      String batch, LocalDate vaccinationTime,
      String comments, String email)
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
