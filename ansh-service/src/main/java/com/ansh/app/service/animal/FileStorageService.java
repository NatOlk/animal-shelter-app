package com.ansh.app.service.animal;

import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;


public interface FileStorageService {

  Optional<String> storeFile(Long id, MultipartFile file, String name,
      String species, String breed, String birthDate);
}
