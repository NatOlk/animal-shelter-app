package com.ansh.app.facade;

import org.springframework.web.multipart.MultipartFile;

/**
 * Facade interface for handling higher-level operations related to animals,
 * combining logic from multiple services if necessary.
 */
public interface AnimalFacade {

  /**
   * Handles the upload of an animal photo and updates the corresponding photo URL
   * in the system. Combines file storage and animal entity update in one method.
   *
   * @param id        the ID of the animal
   * @param file      the photo file to be uploaded
   * @return the URL of the uploaded photo if successful, or an empty string on failure
   */
  String updateAnimalPhoto(Long id, MultipartFile file);
}