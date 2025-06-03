package com.ansh.app.facade.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.ansh.app.service.animal.AnimalService;
import com.ansh.app.service.animal.FileStorageService;
import com.ansh.entity.animal.Animal;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

class AnimalFacadeImplTest {

  private AnimalFacadeImpl animalFacade;

  @Mock
  private FileStorageService fileStorageService;

  @Mock
  private AnimalService animalService;

  @Mock
  private MultipartFile file;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    String animalShelterApp = "https://shelter.example.com";
    animalFacade = new AnimalFacadeImpl(fileStorageService, animalService, animalShelterApp);
  }

  @Test
  void testUpdateAnimalPhoto_success() throws IOException {
    Long id = 1L;
    String relativePath = "uploads/photo.jpg";

    Animal animal = Animal.builder()
        .id(id)
        .name("Dog")
        .species("Species")
        .breed("Breed")
        .birthDate(LocalDate.now())
        .build();

    when(animalService.findById(id)).thenReturn(animal);
    when(fileStorageService.storeFile(anyString(), eq(file))).thenReturn(Optional.of(relativePath));

    String result = animalFacade.updateAnimalPhoto(id, file);

    String expectedUrl = "https://shelter.example.com/uploads/photo.jpg";
    assertEquals(expectedUrl, result);

    verify(animalService).updatePhotoUrl(id, expectedUrl);
  }

  @Test
  void testUpdateAnimalPhoto_failure_throwsException() throws IOException {
    Long id = 2L;

    Animal animal = Animal.builder()
        .id(id)
        .name("Cat")
        .species("Species")
        .breed("Breed")
        .birthDate(LocalDate.now())
        .build();

    when(animalService.findById(id)).thenReturn(animal);
    doThrow(new IOException("Disk full")).when(fileStorageService).storeFile(anyString(), eq(file));

    RuntimeException exception = assertThrows(RuntimeException.class, () ->
        animalFacade.updateAnimalPhoto(id, file)
    );

    assertEquals("Error storing file :java.io.IOException: Disk full", exception.getMessage());
    verify(animalService, never()).updatePhotoUrl(anyLong(), anyString());
  }
}
