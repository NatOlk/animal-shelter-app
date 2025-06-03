package com.ansh.app.service.animal;

import java.io.IOException;
import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service interface for managing storage of photo files related to animal records.
 * <p>
 * This interface defines a contract for saving an uploaded image file and generating
 * a relative path that can later be used to access the file via a URL.
 * The file is typically named using animal-specific data to make it uniquely identifiable.
 * </p>
 */
public interface FileStorageService {

  /**
   * Saves an uploaded photo file to the server using a structured naming convention.
   * <p>
   * The file name is typically based on attributes such as the animal's ID, name,
   * species, breed, and birth date. On successful upload, a relative path to the stored
   * file is returned. If the upload fails, an empty {@code Optional} is returned.
   * </p>
   *
   * @param fileName  a base name for the file
   * @param file      the photo file to upload
   * @return an {@code Optional} containing the relative path to the stored file,
   *         or {@code Optional.empty()} if the file could not be saved
   * @throws IOException if an I/O error occurs while saving the file
   * @throws IllegalArgumentException if file name contains spaces
   */
  Optional<String> storeFile(String fileName, MultipartFile file) throws IOException;
}
