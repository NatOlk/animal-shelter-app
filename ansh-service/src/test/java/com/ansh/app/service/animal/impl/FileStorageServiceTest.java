package com.ansh.app.service.animal.impl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

class FileStorageServiceImplTest {

  private FileStorageServiceImpl fileStorageService;

  @Mock
  private MultipartFile file;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    fileStorageService = new FileStorageServiceImpl();
    fileStorageService.setUploadDir("/tmp");
  }

  @Test
  void storeFile_whenSuccess() throws IOException {
    when(file.isEmpty()).thenReturn(false);
    when(file.getOriginalFilename()).thenReturn("photo.jpg");

    Optional<String> result = fileStorageService.storeFile(1L, file, "Dog", "Labrador", "Black",
        "2022-01-01");

    assertTrue(result.isPresent());
    assertTrue(result.get().contains("1_Dog_Labrador_Black_2022-01-01_1.jpg"));

    verify(file, times(1)).transferTo(any(File.class));
  }

  @Test
  void shouldReturnEmpty_whenFileIsEmpty() throws IOException {
    when(file.isEmpty()).thenReturn(true);

    Optional<String> result = fileStorageService.storeFile(1L, file, "Dog", "Labrador", "Black",
        "2022-01-01");

    assertTrue(result.isEmpty());
    verify(file, never()).transferTo(any(File.class));
  }

  @Test
  void shouldReturnEmpty_whenExtensionIsMissing() throws IOException {
    when(file.isEmpty()).thenReturn(false);
    when(file.getOriginalFilename()).thenReturn("photo");

    Optional<String> result = fileStorageService.storeFile(1L, file, "Dog", "Labrador", "Black",
        "2022-01-01");

    assertTrue(result.isEmpty());
    verify(file, never()).transferTo(any(File.class));
  }

  @Test
  void shouldReturnFile_whenSpaceSymbols() throws IOException {
    when(file.isEmpty()).thenReturn(false);
    when(file.getOriginalFilename()).thenReturn("photo.jpg");

    Optional<String> result = fileStorageService.storeFile(1L, file, "Dog", "Golden Retriever", "Black",
        "2022-01-01");

    assertFalse(result.isEmpty());
    assertTrue(result.get().contains("1_Dog_Golden_Retriever_Black_2022-01-01_1.jpg"));
    verify(file, times(1)).transferTo(any(File.class));
  }

  @Test
  void shouldReturnEmpty_whenIOExceptionOccurs() throws IOException {
    when(file.isEmpty()).thenReturn(false);
    when(file.getOriginalFilename()).thenReturn("photo.jpg");
    doThrow(new IOException("Disk full")).when(file).transferTo(any(File.class));

    Optional<String> result = fileStorageService.storeFile(1L, file, "Dog", "Labrador", "Black",
        "2022-01-01");

    assertTrue(result.isEmpty());
  }
}
