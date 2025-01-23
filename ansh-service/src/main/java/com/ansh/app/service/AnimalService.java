package com.ansh.app.service;

import com.ansh.entity.animal.Animal;
import com.ansh.app.service.notification.animal.AnimalInfoNotificationService;
import com.ansh.repository.AnimalRepository;
import com.ansh.app.service.exception.AnimalCreationException;
import com.ansh.app.service.exception.AnimalNotFoundException;
import com.ansh.app.service.exception.AnimalUpdateException;
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
public class AnimalService {

  private static final Logger LOG = LoggerFactory.getLogger(AnimalService.class);
  private static final String DEFAULT_IMPLANT_CHIP_PATTERN = "00000000-00000000-0000";

  @Autowired
  private AnimalRepository animalRepository;

  @Autowired
  private AnimalInfoNotificationService animalInfoNotificationService;

  public List<Animal> getAllAnimals() {
    return animalRepository.findAllByOrderByNameAsc();
  }

  public Animal findById(Long id) throws AnimalNotFoundException {
    return animalRepository.findById(id)
        .orElseThrow(() -> new AnimalNotFoundException(STR."Animal not found \{id}"));
  }

  public Animal addAnimal(@NonNull String name, @NonNull String species,
      @NonNull String primaryColor, String breed,
      String implantChipId, @NonNull String gender,
      LocalDate birthDate, String pattern) throws AnimalCreationException {
    try {
      Animal animal = new Animal();
      animal.setName(name);
      animal.setSpecies(species);
      animal.setBreed(breed);
      animal.setGender(gender.charAt(0));
      animal.setPattern(pattern);
      animal.setBirthDate(birthDate);
      animal.setAdmissionDate(LocalDate.now());
      animal.setPrimaryColor(primaryColor);
      if (!implantChipId.equals(DEFAULT_IMPLANT_CHIP_PATTERN)) {
        animal.setImplantChipId(implantChipId);
      }
      animalRepository.save(animal);
      LOG.debug("[animal] new : {}", animal);
      animalInfoNotificationService.sendAddAnimalMessage(animal);
      return animal;
    } catch (DataIntegrityViolationException e) {
      if (e.getCause() instanceof ConstraintViolationException) {
        throw new AnimalCreationException("An animal with the same name, species, breed, and gender already exists.");
      }
      throw new AnimalCreationException("Could not create animal due to a database error. Please try again.");
    } catch (Exception e) {
      LOG.error("Unexpected error: ", e);
      throw new AnimalCreationException("An unexpected error occurred. Please contact support.");
    }
  }

  public Animal updateAnimal(@NonNull Long id, String primaryColor,
      String breed, String gender,
      LocalDate birthDate, String pattern)
      throws AnimalNotFoundException, AnimalUpdateException {
    Animal animal = animalRepository.findById(id)
        .orElseThrow(() -> new AnimalNotFoundException(STR."Animal not found \{id}"));

    try {
      if (gender != null) {
        animal.setGender(gender.charAt(0));
      }
      if (birthDate != null) {
        animal.setBirthDate(birthDate);
      }
      if (pattern != null) {
        animal.setPattern(pattern);
      }
      if (primaryColor != null) {
        animal.setPrimaryColor(primaryColor);
      }
      if (breed != null) {
        animal.setBreed(breed);
      }
      LOG.debug("[animal] updated : {}", animal);
      animalRepository.save(animal);
    } catch (Exception e) {
      throw new AnimalUpdateException(STR."Could not update animal:\{e.getMessage()}");
    }

    return animal;
  }

  public Animal removeAnimal(@NonNull Long id, String reason) throws AnimalNotFoundException {
    Animal animal = animalRepository.findById(id)
        .orElseThrow(() -> new AnimalNotFoundException(STR."Animal is not found \{id}"));
    animalRepository.delete(animal);
    LOG.debug("[animal] removed : {}", animal);
    animalInfoNotificationService.sendRemoveAnimalMessage(animal, reason);

    return animal;
  }

  public Animal findAnimalByVaccinationId(Long vaccinationId) {
    return animalRepository.findAnimalByVaccinationId(vaccinationId);
  }
}
