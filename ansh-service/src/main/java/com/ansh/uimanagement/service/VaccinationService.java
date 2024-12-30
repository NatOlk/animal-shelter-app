package com.ansh.uimanagement.service;

import com.ansh.entity.animal.Animal;
import com.ansh.entity.animal.Vaccination;
import com.ansh.notification.NotificationService;
import com.ansh.repository.AnimalRepository;
import com.ansh.repository.VaccinationRepository;
import com.ansh.uimanagement.service.exception.VaccinationCreationException;
import com.ansh.uimanagement.service.exception.VaccinationNotFoundException;
import com.ansh.uimanagement.service.exception.VaccinationUpdateException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class VaccinationService {

  private final SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
  @Autowired
  private VaccinationRepository vaccinationRepository;

  @Autowired
  private AnimalRepository animalRepository;

  @Autowired
  private NotificationService notificationService;

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
      throw new VaccinationCreationException("Animal is not found " + animalId);
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
      notificationService.sendAddVaccinationMessage(vaccination);
      return vaccination;
    } catch (Exception ex) {
      throw new VaccinationCreationException(
          "An error occurred while adding the vaccination for animal " + animalId);
    }
  }

  public Vaccination updateVaccination(@NonNull Long id, String vaccine,
      String batch, LocalDate vaccinationTime,
      String comments, String email)
      throws VaccinationNotFoundException, VaccinationUpdateException {
    //TODO: fix exception
    Vaccination vaccination = vaccinationRepository.findById(id)
        .orElseThrow(() -> new VaccinationNotFoundException("Vaccination not found " + id));

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
    } catch (Exception e) {
      throw new VaccinationUpdateException("Could not update animal:" + e.getMessage());
    }

    return vaccination;
  }

  public Vaccination deleteVaccination(@NonNull Long id) throws VaccinationNotFoundException {
    Vaccination vaccination = vaccinationRepository.findById(id)
        .orElseThrow(() -> new VaccinationNotFoundException("Vaccination not found for: " + id));
    vaccinationRepository.delete(vaccination);
    notificationService.sendRemoveVaccinationMessage(vaccination);
    return vaccination;
  }
}
