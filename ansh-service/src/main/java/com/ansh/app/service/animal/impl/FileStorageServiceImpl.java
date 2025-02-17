package com.ansh.app.service.animal.impl;

import com.ansh.app.service.animal.FileStorageService;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageServiceImpl implements FileStorageService {

  private static final Logger LOG = LoggerFactory.getLogger(FileStorageServiceImpl.class);

  @Value("${file.upload-dir:/uploads}")
  private String uploadDir;

  @Override
  public Optional<String> storeFile(Long id, MultipartFile file, String name, String species, String breed, String birthDate) {
    if (file == null || file.isEmpty()) {
      LOG.warn("Attempted to upload an empty file.");
      return Optional.empty();
    }

    String extension = getFileExtension(file.getOriginalFilename());
    if (extension.isEmpty()) {
      LOG.warn("Invalid file: missing extension");
      return Optional.empty();
    }

    String fileName = formatFileName(id, name, species, breed, birthDate, extension);

    File uploadPath = new File(uploadDir);
    if (!uploadPath.exists() && !uploadPath.mkdirs()) {
      LOG.error("Failed to create upload directory: {}", uploadPath.getAbsolutePath());
      return Optional.empty();
    }

    File destination = new File(uploadPath, fileName);

    try {
      file.transferTo(destination);
      return Optional.of(uploadDir + "/" + fileName);
    } catch (IOException e) {
      LOG.error("Error saving file: {}", e.getMessage());
      return Optional.empty();
    }
  }

  private String getFileExtension(String fileName) {
    int lastIndex = fileName.lastIndexOf('.');
    return (lastIndex == -1) ? "" : fileName.substring(lastIndex + 1);
  }

  private String formatFileName(Long id, String name, String species, String breed, String birthDate, String extension) {
    return STR."\{id}_\{safeName(name)}_\{safeName(species)}_\{safeName(breed)}_\{birthDate}_1.\{extension}";
  }

  private String safeName(String name) {
    return name.replaceAll("[^a-zA-Z0-9]", "_");
  }

  protected void setUploadDir(String uploadDir) {
     this.uploadDir = uploadDir;
  }
}
