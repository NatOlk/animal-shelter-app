package com.ansh.app.controller;

import com.ansh.app.service.AnimalService;
import java.io.File;
import java.io.IOException;
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

  @Value("${file.upload-dir:/uploads/animals}")
  private String uploadDir;

  @Autowired
  private AnimalService animalService;

  @PostMapping("/{id}/upload-photo")
  public ResponseEntity<String> uploadPhoto(
      @PathVariable Long id,
      @RequestParam("file") MultipartFile file,
      @RequestParam String species,
      @RequestParam String name) {

    if (file.isEmpty()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("File is empty");
    }

    String fileName =
        species + "_" + name + "_" + id + "_1." + getFileExtension(file.getOriginalFilename());

    File uploadPath = new File(uploadDir);
    if (!uploadPath.exists()) {
      uploadPath.mkdirs();
    }
    File destination = new File(uploadPath, fileName);

    try {
      file.transferTo(destination);
      String photoUrl = "/uploads/animals/" + fileName;

      animalService.updatePhotoUrl(id, photoUrl);

      return ResponseEntity.ok("Photo uploaded successfully: " + photoUrl);
    } catch (IOException e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Failed to upload photo");
    }
  }

  private String getFileExtension(String fileName) {
    int lastIndex = fileName.lastIndexOf('.');
    return lastIndex == -1 ? "" : fileName.substring(lastIndex + 1);
  }
}
