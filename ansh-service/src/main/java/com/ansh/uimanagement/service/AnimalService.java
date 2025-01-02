package com.ansh.uimanagement.service;

import com.ansh.entity.animal.Animal;
import com.ansh.notification.NotificationService;
import com.ansh.repository.AnimalRepository;
import com.ansh.uimanagement.service.exception.AnimalCreationException;
import com.ansh.uimanagement.service.exception.AnimalNotFoundException;
import com.ansh.uimanagement.service.exception.AnimalUpdateException;
import java.time.LocalDate;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class AnimalService {

  private static final Logger LOG = LoggerFactory.getLogger(AnimalService.class);
  private final String DEFAULT_IMPLANT_CHIP_PATTERN = "00000000-00000000-0000";

  @Autowired
  private AnimalRepository animalRepository;

  @Autowired
  private NotificationService notificationService;

  public List<Animal> getAllAnimals() {
    return animalRepository.findAllByOrderByNameAsc();
  }

  public Animal findById(Long id) throws AnimalNotFoundException {
    return animalRepository.findById(id)
        .orElseThrow(() -> new AnimalNotFoundException("Animal not found " + id));
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

      notificationService.sendAddAnimalMessage(animal);
      return animal;
    } catch (Exception e) {
      throw new AnimalCreationException("Could not create animal: " + e.getMessage());
    }
  }

  public Animal updateAnimal(@NonNull Long id, String primaryColor,
      String breed, String gender,
      LocalDate birthDate, String pattern)
      throws AnimalNotFoundException, AnimalUpdateException {
    Animal animal = animalRepository.findById(id)
        .orElseThrow(() -> new AnimalNotFoundException("Animal not found " + id));

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
      animalRepository.save(animal);
    } catch (Exception e) {
      throw new AnimalUpdateException("Could not update animal:" + e.getMessage());
    }

    return animal;
  }

  public Animal removeAnimal(@NonNull Long id, String reason) throws AnimalNotFoundException {
    Animal animal = animalRepository.findById(id)
        .orElseThrow(() -> new AnimalNotFoundException("Animal is not found " + id));
    animalRepository.delete(animal);
    notificationService.sendRemoveAnimalMessage(animal, reason);

    return animal;
  }

  public Animal findAnimalByVaccinationId(Long vaccinationId) {
    return animalRepository.findAnimalByVaccinationId(vaccinationId);
  }
}
