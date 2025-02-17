package com.ansh.app.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ansh.app.service.animal.FileStorageService;
import com.ansh.app.service.animal.impl.AnimalServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
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
  private AnimalServiceImpl animalService;

  @Mock
  private FileStorageService fileStorageService;

  @Mock
  private MultipartFile file;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testUploadPhoto_Success() {
    String expectedPhotoUrl = "/uploads/1_Dog_Labrador_Black_2022-01-01_1.jpg";

    when(file.isEmpty()).thenReturn(false);
    when(fileStorageService.storeFile(1L, file, "Dog", "Labrador", "Black", "2022-01-01"))
        .thenReturn(Optional.of(expectedPhotoUrl));

    ResponseEntity<String> response = controller.uploadPhoto(
        1L, file, "Dog", "Labrador", "Black", "2022-01-01"
    );

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(expectedPhotoUrl, response.getBody());

    ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
    verify(animalService).updatePhotoUrl(eq(1L), captor.capture());
    assertEquals(expectedPhotoUrl, captor.getValue());
  }

  @Test
  void testUploadPhoto_whenStorageFails() {
    when(file.isEmpty()).thenReturn(false);
    when(fileStorageService.storeFile(1L, file, "Dog", "Labrador", "Black", "2022-01-01"))
        .thenReturn(Optional.empty());

    ResponseEntity<String> response = controller.uploadPhoto(
        1L, file, "Dog", "Labrador", "Black", "2022-01-01"
    );

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertEquals("Failed to upload photo", response.getBody());

    verify(animalService, never()).updatePhotoUrl(anyLong(), anyString());
  }

  @Test
  void testUploadPhoto_whenComplexNames() {
    String expectedPhotoUrl = "/uploads/1_Dog_123_Labrador_22_Black_2022-01-01_1.jpg";

    when(file.isEmpty()).thenReturn(false);
    when(fileStorageService.storeFile(1L, file, "Dog 123", "Labrador 22", "Black", "2022-01-01"))
        .thenReturn(Optional.of(expectedPhotoUrl));

    ResponseEntity<String> response = controller.uploadPhoto(
        1L, file, "Dog 123", "Labrador 22", "Black", "2022-01-01"
    );

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(expectedPhotoUrl, response.getBody());
  }
}
