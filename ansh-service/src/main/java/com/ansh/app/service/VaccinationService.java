package com.ansh.app.service;

import com.ansh.entity.animal.Animal;
import com.ansh.entity.animal.Vaccination;
import com.ansh.app.service.notification.animal.AnimalInfoNotificationService;
import com.ansh.repository.AnimalRepository;
import com.ansh.repository.VaccinationRepository;
import com.ansh.app.service.exception.animal.VaccinationCreationException;
import com.ansh.app.service.exception.animal.VaccinationNotFoundException;
import com.ansh.app.service.exception.animal.VaccinationUpdateException;
import java.time.LocalDate;
import java.util.List;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class VaccinationService {
  private static final Logger LOG = LoggerFactory.getLogger(VaccinationService.class);

  @Autowired
  private VaccinationRepository vaccinationRepository;

  @Autowired
  private AnimalRepository animalRepository;

  @Autowired
  private AnimalInfoNotificationService animalInfoNotificationService;

  public List<Vaccination> getAllVaccinations() {
    return vaccinationRepository.findAll();
  }

  public List<Vaccination> findByAnimalId(Long animalId) {
    return vaccinationRepository.findByAnimalId(animalId);
  }

  public int vaccinationCountById(Long id) {
    return vaccinationRepository.findVaccinationCountByAnimalId(id);
  }

  public Vaccination addVaccination(@NonNull Long animalId, @NonNull String vaccine,
      @NonNull String batch, @NonNull LocalDate vaccinationTime,
      String comments, @NonNull String email) throws VaccinationCreationException {

    Animal animal = animalRepository.findById(animalId).orElse(null);
    if (animal == null) {
      throw new VaccinationCreationException(STR."Animal is not found \{animalId}");
    }

    try {
      Vaccination vaccination = new Vaccination();
      vaccination.setVaccine(vaccine);
      vaccination.setBatch(batch);
      vaccination.setVaccinationTime(vaccinationTime);
      vaccination.setComments(comments);
      vaccination.setEmail(email);
      vaccination.setAnimal(animal);

      vaccinationRepository.save(vaccination);
      LOG.debug("[vaccination] added : {}", vaccination);
      animalInfoNotificationService.sendAddVaccinationMessage(vaccination);
      return vaccination;
    } catch (DataIntegrityViolationException e) {
      if (e.getCause() instanceof ConstraintViolationException) {
        throw new VaccinationCreationException("A vaccination with the same name, batch already exists.");
      }
      throw new VaccinationCreationException("Could not create vaccination due to a database error. Please try again.");
    } catch (Exception e) {
      LOG.error("Unexpected error: ", e);
      throw new VaccinationCreationException(
          STR."An error occurred while adding the vaccination for animal \{animalId}");
    }
  }

  public Vaccination updateVaccination(@NonNull Long id, String vaccine,
      String batch, LocalDate vaccinationTime,
      String comments, String email)
      throws VaccinationNotFoundException, VaccinationUpdateException {
    Vaccination vaccination = vaccinationRepository.findById(id)
        .orElseThrow(() -> new VaccinationNotFoundException(STR."Vaccination not found \{id}"));

    try {
      if (vaccine != null) {
        vaccination.setVaccine(vaccine);
      }
      if (batch != null) {
        vaccination.setBatch(batch);
      }

      if (vaccinationTime != null) {
        vaccination.setVaccinationTime(vaccinationTime);
      }
      if (comments != null) {
        vaccination.setComments(comments);
      }
      if (email != null) {
        vaccination.setEmail(email);
      }
      vaccinationRepository.save(vaccination);
      LOG.debug("[vaccination] updated : {}", vaccination);
    } catch (Exception e) {
      throw new VaccinationUpdateException(STR."Could not update animal:\{e.getMessage()}");
    }

    return vaccination;
  }

  public Vaccination deleteVaccination(@NonNull Long id) throws VaccinationNotFoundException {
    Vaccination vaccination = vaccinationRepository.findById(id)
        .orElseThrow(() -> new VaccinationNotFoundException(STR."Vaccination not found for: \{id}"));
    vaccinationRepository.delete(vaccination);
    LOG.debug("[vaccination] removed : {}", vaccination);
    animalInfoNotificationService.sendRemoveVaccinationMessage(vaccination);
    return vaccination;
  }
}
