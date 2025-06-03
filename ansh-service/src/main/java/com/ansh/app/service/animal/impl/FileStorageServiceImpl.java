package com.ansh.app.service.animal.impl;

import com.ansh.app.service.animal.FileStorageService;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
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
  public Optional<String> storeFile(String fileName, MultipartFile file) throws IOException {
    if (file == null || file.isEmpty()) {
      LOG.warn("Attempted to upload an empty file.");
      return Optional.empty();
    }

    if (fileName.contains(" ")) {
      throw new IllegalArgumentException("File name must not contain spaces");
    }

    String extension = getFileExtension(Objects.requireNonNull(file.getOriginalFilename()));
    if (extension.isEmpty()) {
      LOG.warn("Invalid file: missing extension");
      return Optional.empty();
    }

    String fileNameExt = concatFileNameAndExtension(fileName, extension);

    File uploadPath = new File(uploadDir);
    if (!uploadPath.exists() && !uploadPath.mkdirs()) {
      LOG.error("Failed to create upload directory: {}", uploadPath.getAbsolutePath());
      return Optional.empty();
    }

    File destination = new File(uploadPath, fileNameExt);

    file.transferTo(destination);
    return Optional.of(STR."\{uploadDir}/\{fileNameExt}");
  }

  private String getFileExtension(String fileName) {
    int lastIndex = fileName.lastIndexOf('.');
    return (lastIndex == -1) ? "" : fileName.substring(lastIndex + 1);
  }

  private String concatFileNameAndExtension(String fileName, String extension) {
    return STR."\{fileName}.\{extension}";
  }

  protected void setUploadDir(String uploadDir) {
    this.uploadDir = uploadDir;
  }
}
