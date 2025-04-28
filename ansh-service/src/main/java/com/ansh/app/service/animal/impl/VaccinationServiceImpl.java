package com.ansh.app.service.animal.impl;

import com.ansh.app.service.animal.VaccinationService;
import com.ansh.app.service.exception.animal.VaccinationCreationException;
import com.ansh.app.service.exception.animal.VaccinationNotFoundException;
import com.ansh.app.service.exception.animal.VaccinationUpdateException;
import com.ansh.app.service.notification.animal.AnimalInfoNotificationService;
import com.ansh.dto.UpdateVaccinationInput;
import com.ansh.dto.VaccinationInput;
import com.ansh.entity.animal.Animal;
import com.ansh.entity.animal.Vaccination;
import com.ansh.repository.AnimalRepository;
import com.ansh.repository.VaccinationRepository;
import jakarta.transaction.Transactional;
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
public class VaccinationServiceImpl implements VaccinationService {

  private static final Logger LOG = LoggerFactory.getLogger(VaccinationServiceImpl.class);

  @Autowired
  private VaccinationRepository vaccinationRepository;

  @Autowired
  private AnimalRepository animalRepository;

  @Autowired
  private AnimalInfoNotificationService animalInfoNotificationService;

  @Override
  @Cacheable(value = "vaccinations", key = "'allVaccinations'")
  public List<Vaccination> getAllVaccinations() {
    return vaccinationRepository.findAll();
  }

  @Override
  @Cacheable(value = "vaccinations", key = "'animal-' + #animalId")
  public List<Vaccination> findByAnimalId(Long animalId) {
    return vaccinationRepository.findByAnimalId(animalId);
  }

  @Override
  public int vaccinationCountById(Long id) {
    return vaccinationRepository.findVaccinationCountByAnimalId(id);
  }

  @Override
  @CachePut(value = "vaccination", key = "#result.id")
  @Caching(evict = {
      @CacheEvict(value = "vaccinations", key = "'allVaccinations'"),
      @CacheEvict(value = "vaccinations", key = "'animal-' + #vaccination.animalId")
  })
  public Vaccination addVaccination(@NonNull VaccinationInput vaccination)
      throws VaccinationCreationException {

    Animal animal = animalRepository.findById(vaccination.getAnimalId()).orElse(null);
    if (animal == null) {
      throw new VaccinationCreationException(
          STR."Animal is not found \{vaccination.getAnimalId()}");
    }

    try {
      Vaccination entity = Vaccination.builder()
          .vaccine(vaccination.getVaccine())
          .batch(vaccination.getBatch())
          .vaccinationTime(vaccination.getVaccinationTime())
          .comments(vaccination.getComments())
          .email(vaccination.getEmail())
          .animal(animal)
          .build();
      vaccinationRepository.save(entity);
      LOG.debug("[vaccination] added : {}", vaccination);
      animalInfoNotificationService.sendAddVaccinationMessage(entity);
      return entity;
    } catch (DataIntegrityViolationException e) {
      if (e.getCause() instanceof ConstraintViolationException) {
        throw new VaccinationCreationException(
            "A vaccination with the same name, batch already exists.");
      }
      throw new VaccinationCreationException(
          "Could not create vaccination due to a database error. Please try again.");
    } catch (Exception e) {
      LOG.error("Unexpected error: ", e);
      throw new VaccinationCreationException(
          STR."An error occurred while adding the vaccination for animal \{vaccination.getAnimalId()}");
    }
  }

  @Override
  @Transactional
  @CachePut(value = "vaccination", key = "#vaccination.id")
  @CacheEvict(value = "vaccinations", key = "'allVaccinations'")
  public Vaccination updateVaccination(@NonNull UpdateVaccinationInput vaccination)
      throws VaccinationNotFoundException, VaccinationUpdateException {
    Vaccination entity = vaccinationRepository.findById(vaccination.getId())
        .orElseThrow(() -> new VaccinationNotFoundException(
            STR."Vaccination not found \{vaccination.getId()}"));

    try {
      if (vaccination.getVaccine() != null) {
        entity.setVaccine(vaccination.getVaccine());
      }
      if (vaccination.getBatch() != null) {
        entity.setBatch(vaccination.getBatch());
      }

      if (vaccination.getVaccinationTime() != null) {
        entity.setVaccinationTime(vaccination.getVaccinationTime());
      }
      if (vaccination.getComments() != null) {
        entity.setComments(vaccination.getComments());
      }
      if (vaccination.getEmail() != null) {
        entity.setEmail(vaccination.getEmail());
      }
      vaccinationRepository.save(entity);
      LOG.debug("[vaccination] updated : {}", vaccination);
    } catch (ObjectOptimisticLockingFailureException oe) {
      throw new VaccinationUpdateException(
          "Update conflict! Another user modified this vaccination.");
    } catch (Exception e) {
      throw new VaccinationUpdateException(STR."Could not update animal:\{e.getMessage()}");
    }

    return entity;
  }

  @Override
  @Caching(evict = {
      @CacheEvict(value = "vaccination", key = "#id"),
      @CacheEvict(value = "vaccinations", key = "'allVaccinations'"),
      @CacheEvict(value = "vaccinations", key = "'animal-' + #animalId")
  })
  public Vaccination deleteVaccination(@NonNull Long id) throws VaccinationNotFoundException {
    Vaccination vaccination = vaccinationRepository.findById(id)
        .orElseThrow(
            () -> new VaccinationNotFoundException(STR."Vaccination not found for: \{id}"));
    Long animalId = vaccination.getAnimal().getId();
    vaccinationRepository.delete(vaccination);
    LOG.debug("[vaccination] removed : {}", vaccination);
    animalInfoNotificationService.sendRemoveVaccinationMessage(vaccination);
    return vaccination;
  }
}
