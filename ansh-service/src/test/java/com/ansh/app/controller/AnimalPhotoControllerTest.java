package com.ansh.app.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ansh.app.facade.AnimalFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

class AnimalPhotoControllerTest {

  @InjectMocks
  private AnimalPhotoController controller;

  @Mock
  private AnimalFacade animalFacade;

  @Mock
  private MultipartFile file;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testUploadPhoto_Success() {
    Long animalId = 1L;
    String expectedPhotoUrl = "https://shelter.example.com/uploads/animal.jpg";

    when(animalFacade.updateAnimalPhoto(animalId, file)).thenReturn(expectedPhotoUrl);

    ResponseEntity<String> response = controller.uploadPhoto(animalId, file);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(expectedPhotoUrl, response.getBody());
    verify(animalFacade).updateAnimalPhoto(animalId, file);
  }

  @Test
  void testUploadPhoto_WithDifferentAnimalId() {
    Long animalId = 42L;
    String expectedUrl = "https://shelter.example.com/uploads/photo42.jpg";

    when(animalFacade.updateAnimalPhoto(animalId, file)).thenReturn(expectedUrl);

    ResponseEntity<String> response = controller.uploadPhoto(animalId, file);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(expectedUrl, response.getBody());
    verify(animalFacade).updateAnimalPhoto(animalId, file);
  }
}