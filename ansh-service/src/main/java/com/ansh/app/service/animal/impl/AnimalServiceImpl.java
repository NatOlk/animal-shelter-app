package com.ansh.app.service.animal.impl;

import com.ansh.app.service.animal.AnimalService;
import com.ansh.app.service.exception.animal.AnimalCreationException;
import com.ansh.app.service.exception.animal.AnimalNotFoundException;
import com.ansh.app.service.exception.animal.AnimalUpdateException;
import com.ansh.app.service.notification.animal.AnimalInfoNotificationService;
import com.ansh.dto.AnimalInput;
import com.ansh.dto.UpdateAnimalInput;
import com.ansh.entity.animal.Animal;
import com.ansh.repository.AnimalRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.lang.NonNull;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

@Service
public class AnimalServiceImpl implements AnimalService {

  private static final Logger LOG = LoggerFactory.getLogger(AnimalServiceImpl.class);
  private static final String DEFAULT_IMPLANT_CHIP_PATTERN = "00000000-00000000-0000";

  @Autowired
  private AnimalRepository animalRepository;

  @Autowired
  private AnimalInfoNotificationService animalInfoNotificationService;

  @Override
  @Cacheable(value = "animals", key = "'allAnimals'")
  public List<Animal> getAllAnimals() {
    return animalRepository.findAllByOrderByNameAsc();
  }

  @Override
  @Cacheable(value = "animal", key = "#id")
  public Animal findById(Long id) throws AnimalNotFoundException {
    return animalRepository.findById(id)
        .orElseThrow(() -> new AnimalNotFoundException(STR."Animal not found \{id}"));
  }

  @Override
  @CachePut(value = "animal", key = "#result.id")
  @CacheEvict(value = "animals", allEntries = true)
  public Animal addAnimal(@NonNull AnimalInput animal) throws AnimalCreationException {
    try {
      Animal entity = Animal.builder()
          .name(animal.getName())
          .species(animal.getSpecies())
          .breed(animal.getBreed())
          .gender(animal.getGender().charAt(0))
          .pattern(animal.getPattern())
          .birthDate(animal.getBirthDate())
          .admissionDate(LocalDate.now())
          .primaryColor(animal.getPrimaryColor()).build();
      if (!DEFAULT_IMPLANT_CHIP_PATTERN.equals(animal.getImplantChipId())) {
        entity.setImplantChipId(animal.getImplantChipId());
      }
      animalRepository.save(entity);
      LOG.debug("[animal] new : {}", entity);
      animalInfoNotificationService.sendAddAnimalMessage(entity);
      return entity;
    } catch (DataIntegrityViolationException e) {
      if (e.getCause() instanceof ConstraintViolationException) {
        throw new AnimalCreationException(
            "An animal with the same name, species, breed, and gender already exists.");
      }
      throw new AnimalCreationException(
          "Could not create animal due to a database error. Please try again.");
    } catch (Exception e) {
      LOG.error("Unexpected error: ", e);
      throw new AnimalCreationException("An unexpected error occurred. Please contact support.");
    }
  }

  @Override
  @Transactional
  @CachePut(value = "animal", key = "#animal.id")
  @CacheEvict(value = "animals", allEntries = true)
  public Animal updateAnimal(@NonNull UpdateAnimalInput animal)
      throws AnimalNotFoundException, AnimalUpdateException {
    Animal entity = animalRepository.findById(animal.getId())
        .orElseThrow(() -> new AnimalNotFoundException(STR."Animal not found \{animal.getId()}"));

    try {
      if (animal.getGender() != null && !animal.getGender().isEmpty()) {
        entity.setGender(animal.getGender().charAt(0));
      }
      if (animal.getBirthDate() != null) {
        entity.setBirthDate(animal.getBirthDate());
      }
      if (animal.getPattern() != null) {
        entity.setPattern(animal.getPattern());
      }
      if (animal.getPrimaryColor() != null) {
        entity.setPrimaryColor(animal.getPrimaryColor());
      }
      if (animal.getBreed() != null) {
        entity.setBreed(animal.getBreed());
      }
      if (animal.getPhotoImgPath() != null) {
        entity.setPhotoImgPath(animal.getPhotoImgPath());
      }
      LOG.debug("[animal] updated : {}", animal);
      animalRepository.save(entity);
    } catch (ObjectOptimisticLockingFailureException oe) {
      throw new AnimalUpdateException("Update conflict! Another user modified this animal.");
    } catch (Exception e) {
      throw new AnimalUpdateException(STR."Could not update animal:\{e.getMessage()}");
    }

    return entity;
  }

  @Override
  @Caching(evict = {
      @CacheEvict(value = "animal", key = "#id"),
      @CacheEvict(value = "animals", allEntries = true)
  })
  public Animal removeAnimal(@NonNull Long id, String reason) throws AnimalNotFoundException {
    Animal animal = animalRepository.findById(id)
        .orElseThrow(() -> new AnimalNotFoundException(STR."Animal is not found \{id}"));
    animalRepository.delete(animal);
    LOG.debug("[animal] removed : {}", animal);
    animalInfoNotificationService.sendRemoveAnimalMessage(animal, reason);

    return animal;
  }

  @Override
  public Animal findAnimalByVaccinationId(Long vaccinationId) {
    return animalRepository.findAnimalByVaccinationId(vaccinationId);
  }

  @Override
  @Transactional
  @CachePut(value = "animal", key = "#id")
  public void updatePhotoUrl(Long id, String path) {
    animalRepository.updatePhotoPathById(id, path);
  }
}
