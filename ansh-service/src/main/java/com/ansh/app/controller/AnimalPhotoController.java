package com.ansh.app.controller;

import com.ansh.app.service.AnimalService;
import java.io.File;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class AnimalPhotoController {

  private static final Logger LOG = LoggerFactory.getLogger(AnimalPhotoController.class);

  @Value("${file.upload-dir:/uploads}")
  private String uploadDir;

  @Autowired
  private AnimalService animalService;

  @PostMapping("/{id}/upload-photo")
  public ResponseEntity<String> uploadPhoto(
      @PathVariable Long id,
      @RequestParam("file") MultipartFile file,
      @RequestParam String name,
      @RequestParam String species,
      @RequestParam String breed,
      @RequestParam String birthDate) {

    if (file == null || file.isEmpty()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("File is empty");
    }

    String fileName =
        STR."\{id}_\{name}_\{species}_\{breed}_\{birthDate}_1.\{getFileExtension(
            file.getOriginalFilename())}";

    File uploadPath = new File(uploadDir);
    if (!uploadPath.exists() && !uploadPath.mkdirs()) {
      LOG.error("Failed to create directory: {}", uploadPath.getAbsolutePath());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Failed to create upload directory");
    }

    File destination = new File(uploadPath, fileName);

    try {
      file.transferTo(destination);
      String photoUrl = STR."\{uploadDir}/\{fileName}";

      animalService.updatePhotoUrl(id, photoUrl);

      return ResponseEntity.ok(photoUrl);
    } catch (IOException e) {
      LOG.error("Error saving file: {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Failed to upload photo");
    }
  }

  private String getFileExtension(String fileName) {
    if (fileName == null) {
      return ".jpeg";
    }
    int lastIndex = fileName.lastIndexOf('.');
    return lastIndex == -1 ? "" : fileName.substring(lastIndex + 1);
  }
}
