package com.ansh.app.service.animal.impl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

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

    Optional<String> result = fileStorageService.storeFile("Dog_1_photo", file);

    assertTrue(result.isPresent());
    assertTrue(result.get().contains("Dog_1_photo"));
    verify(file, times(1)).transferTo(any(File.class));
  }

  @Test
  void shouldReturnEmpty_whenFileIsEmpty() throws IOException {
    when(file.isEmpty()).thenReturn(true);

    Optional<String> result = fileStorageService.storeFile("Dog_1_photo", file);

    assertTrue(result.isEmpty());
    verify(file, never()).transferTo(any(File.class));
  }

  @Test
  void shouldReturnEmpty_whenExtensionIsMissing() throws IOException {
    when(file.isEmpty()).thenReturn(false);
    when(file.getOriginalFilename()).thenReturn("photo");

    Optional<String> result = fileStorageService.storeFile("Dog_1_photo", file);

    assertTrue(result.isEmpty());
    verify(file, never()).transferTo(any(File.class));
  }

  @Test
  void shouldReturnFile_whenNameHasSpaces() throws IOException {
    when(file.isEmpty()).thenReturn(false);
    when(file.getOriginalFilename()).thenReturn("photo.jpg");

    assertThrows(IllegalArgumentException.class, () -> {
      fileStorageService.storeFile("Dog Golden Retriever 1", file);
    });
  }

  @Test
  void shouldThrowIOException_whenTransferFails() throws IOException {
    when(file.isEmpty()).thenReturn(false);
    when(file.getOriginalFilename()).thenReturn("photo.jpg");
    doThrow(new IOException("Disk full")).when(file).transferTo(any(File.class));

    assertThrows(IOException.class, () -> {
      fileStorageService.storeFile("Dog_1_photo", file);
    });
  }
}