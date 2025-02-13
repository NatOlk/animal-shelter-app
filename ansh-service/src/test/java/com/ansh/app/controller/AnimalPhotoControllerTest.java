package com.ansh.app.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ansh.app.service.animal.impl.AnimalServiceImpl;
import java.io.File;
import java.io.IOException;
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
  private MultipartFile file;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    controller.setUploadDir("/tmp/uploads");

  }

  @Test
  void testUploadPhoto_Success() {
    when(file.isEmpty()).thenReturn(false);
    when(file.getOriginalFilename()).thenReturn("photo.jpg");

    ResponseEntity<String> response = controller.uploadPhoto(
        1L, file, "Dog", "Labrador", "Black", "2022-01-01"
    );

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertTrue(response.getBody().contains("/uploads"));

    ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
    verify(animalService).updatePhotoUrl(eq(1L), captor.capture());

    String savedPhotoUrl = captor.getValue();
    assertTrue(savedPhotoUrl.contains("1_Dog_Labrador_Black_2022-01-01"));
  }

  @Test
  void testUploadPhoto_whenEmptyFile() {
    when(file.isEmpty()).thenReturn(true);

    ResponseEntity<String> response = controller.uploadPhoto(
        1L, file, "Dog", "Labrador", "Black", "2022-01-01"
    );

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("File is empty", response.getBody());
  }

  @Test
  void testUploadPhoto_whenIOException() throws IOException {
    when(file.isEmpty()).thenReturn(false);
    when(file.getOriginalFilename()).thenReturn("photo.jpg");

    doThrow(new IOException("Disk full")).when(file).transferTo(any(File.class));

    ResponseEntity<String> response = controller.uploadPhoto(
        1L, file, "Dog", "Labrador", "Black", "2022-01-01"
    );

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertEquals("Failed to upload photo", response.getBody());
  }

  @Test
  void testUploadPhoto_whenExtensionIsEmpty() {
    when(file.isEmpty()).thenReturn(false);
    when(file.getOriginalFilename()).thenReturn("photo");

    ResponseEntity<String> response = controller.uploadPhoto(
        1L, file, "Dog", "Labrador", "Black", "2022-01-01"
    );

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("Invalid file: missing extension", response.getBody());
  }

  @Test
  void testUploadPhoto_whenComplexNames() {
    when(file.isEmpty()).thenReturn(false);
    when(file.getOriginalFilename()).thenReturn("photo.jpg");

    ResponseEntity<String> response = controller.uploadPhoto(
        1L, file, "Dog 123", "Labrador 22", "Black", "2022-01-01"
    );
    assertNotNull(response.getBody());
    assertTrue(response.getBody().contains("1_Dog_123_Labrador_22_Black_2022-01-01_1.jpg"));
  }
}
