package com.ansh.app.facade.impl;

import com.ansh.app.facade.AnimalFacade;
import com.ansh.app.service.animal.AnimalService;
import com.ansh.app.service.animal.FileStorageService;
import com.ansh.app.service.exception.animal.AnimalPhotoNotStored;
import com.ansh.entity.animal.Animal;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AnimalFacadeImpl implements AnimalFacade {

  private static final Logger LOG = LoggerFactory.getLogger(AnimalFacadeImpl.class);
  private final FileStorageService fileStorageService;
  private final AnimalService animalService;
  private final String animalShelterApp;

  public AnimalFacadeImpl(FileStorageService fileStorageService,
      AnimalService animalService,
      @Value("${app.animalShelterApp}") String animalShelterApp) {
    this.fileStorageService = fileStorageService;
    this.animalService = animalService;
    this.animalShelterApp = animalShelterApp;
  }


  @Override
  @Transactional
  public String updateAnimalPhoto(Long id, MultipartFile file) {
    try {
      Animal animal = animalService.findById(id);
      Optional<String> storedPath = fileStorageService.storeFile(formatFileName(animal), file);
      if (storedPath.isEmpty()) {
        LOG.warn("File storage returned empty for animal ID: {}", id);
        return "";
      }
      String fullUrl = STR."\{animalShelterApp}/\{storedPath.get()}";
      animalService.updatePhotoUrl(id, fullUrl);
      return fullUrl;
    } catch (Exception e) {
      LOG.error("Error occurred while storing file: {}", e.getMessage());
      throw new AnimalPhotoNotStored(STR."Error storing file :\{e}");
    }
  }

  private String formatFileName(Animal animal) {
    return STR."\{animal.getId()}_\{safeName(animal.getName())}_\{
        safeName(animal.getSpecies())}_\{safeName(animal.getBreed())}_\{animal.getBirthDate()}_1";
  }

  private String safeName(String name) {
    return name.replaceAll("[^a-zA-Z0-9]", "_");
  }

}
