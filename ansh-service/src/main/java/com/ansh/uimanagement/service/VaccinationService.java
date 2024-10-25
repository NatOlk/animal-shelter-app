package com.ansh.uimanagement.service;

import com.ansh.entity.Animal;
import com.ansh.entity.Vaccination;
import com.ansh.notification.NotificationService;
import com.ansh.repository.AnimalRepository;
import com.ansh.repository.VaccinationRepository;
import com.ansh.uimanagement.service.exception.VaccinationCreationException;
import com.ansh.uimanagement.service.exception.VaccinationNotFoundException;
import com.ansh.uimanagement.service.exception.VaccinationUpdateException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class VaccinationService {

  private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

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

  public Optional<Vaccination> findByKey(Long id) {
    return vaccinationRepository.findById(id);
  }

  public Vaccination addVaccination(@NonNull Long animalId, @NonNull String vaccine,
      @NonNull String batch, @NonNull String vaccinationTime,
      String comments, @NonNull String email) throws VaccinationCreationException {
    try {
      Animal animal = animalRepository.findById(animalId).orElse(null);
      if (animal == null) {
        throw new VaccinationCreationException("Animal is not found " + animalId);
      }

      Vaccination vaccination = new Vaccination();
      vaccination.setVaccine(vaccine);
      vaccination.setBatch(batch);
      vaccination.setVaccinationTime(formatter.parse(vaccinationTime));
      vaccination.setComments(comments);
      vaccination.setEmail(email);
      vaccination.setAnimal(animal);
      //	vaccination.setVaccination_time(new Date());

      vaccinationRepository.save(vaccination);
      notificationService.sendAddVaccinationMessage(vaccination);

      return vaccination;
    } catch (Exception e) {
      throw new VaccinationCreationException("Error during add vaccination : ");
    }
  }

  public Vaccination updateVaccination(@NonNull Long id, String vaccine,
      String batch, String vaccinationTime,
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
        vaccination.setVaccinationTime(formatter.parse(vaccinationTime));
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
    return vaccination;
  }
}
