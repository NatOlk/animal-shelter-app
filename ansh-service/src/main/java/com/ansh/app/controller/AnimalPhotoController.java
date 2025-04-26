package com.ansh.app.controller;

import com.ansh.app.service.animal.AnimalService;
import com.ansh.app.service.animal.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class AnimalPhotoController {

  private static final Logger LOG = LoggerFactory.getLogger(AnimalPhotoController.class);

  @Autowired
  private AnimalService animalService;

  @Autowired
  private FileStorageService fileStorageService;

  @PostMapping("/{id}/upload-photo")
  public ResponseEntity<String> uploadPhoto(
      @PathVariable Long id,
      @RequestParam("file") MultipartFile file,
      @RequestParam String name,
      @RequestParam String species,
      @RequestParam String breed,
      @RequestParam String birthDate) {

    return fileStorageService.storeFile(id, file, name, species, breed, birthDate)
        .map(photoUrl -> {
          animalService.updatePhotoUrl(id, photoUrl);
          return ResponseEntity.ok(photoUrl);
        })
        .orElseGet(() -> ResponseEntity.internalServerError().body("Failed to upload photo"));
  }
}
