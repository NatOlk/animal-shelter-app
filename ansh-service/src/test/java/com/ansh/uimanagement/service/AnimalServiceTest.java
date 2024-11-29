package com.ansh.uimanagement.service;

import static graphql.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ansh.entity.animal.Animal;
import com.ansh.notification.NotificationService;
import com.ansh.repository.AnimalRepository;
import com.ansh.uimanagement.service.exception.AnimalCreationException;
import com.ansh.uimanagement.service.exception.AnimalNotFoundException;
import com.ansh.uimanagement.service.exception.AnimalUpdateException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class AnimalServiceTest {

  @Mock
  private AnimalRepository animalRepository;

  @Mock
  private NotificationService notificationService;

  @InjectMocks
  private AnimalService animalService;

  private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void shouldReturnAllAnimalsInOrder() {
    Animal animal1 = new Animal();
    animal1.setName("Bella");
    Animal animal2 = new Animal();
    animal2.setName("Charlie");

    when(animalRepository.findAllByOrderByNameAsc()).thenReturn(Arrays.asList(animal1, animal2));

    var animals = animalService.getAllAnimals();

    assertEquals(2, animals.size());
    verify(animalRepository, times(1)).findAllByOrderByNameAsc();
  }

  @Test
  void shouldFindAnimalById_whenExists() throws AnimalNotFoundException {
    Animal animal = new Animal();
    animal.setId(1L);

    when(animalRepository.findById(1L)).thenReturn(Optional.of(animal));

    var foundAnimal = animalService.findById(1L);

    assertEquals(1L, foundAnimal.getId());
    verify(animalRepository, times(1)).findById(1L);
  }

  @Test
  void shouldThrowException_whenAnimalByIdNotFound() {
    when(animalRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(AnimalNotFoundException.class, () -> animalService.findById(1L));
    verify(animalRepository, times(1)).findById(1L);
  }

  @Test
  void shouldAddAnimalSuccessfully() throws Exception {
    Animal animal = new Animal();
    animal.setName("Bella");
    animal.setSpecies("Dog");

    when(animalRepository.save(any(Animal.class))).thenReturn(animal);

    var addedAnimal = animalService.addAnimal(
        "Bella", "Dog", "Brown", "Labrador",
        "12345", "F", "2022-01-01", "Spotted");

    assertNotNull(addedAnimal);
    assertEquals("Bella", addedAnimal.getName());
    verify(animalRepository, times(1)).save(any(Animal.class));
    verify(notificationService, times(1)).sendAddAnimalMessage(any(Animal.class));
  }

  @Test
  void shouldThrowException_whenAddAnimalFails() {
    when(animalRepository.save(any(Animal.class))).thenThrow(
        new RuntimeException("Database error"));

    assertThrows(AnimalCreationException.class, () -> animalService.addAnimal(
        "Bella", "Dog", "Brown", "Labrador",
        "12345", "F", "2022-01-01", "Spotted"));

    verify(animalRepository, times(1)).save(any(Animal.class));
    verify(notificationService, never()).sendAddAnimalMessage(any(Animal.class));
  }

  @Test
  void shouldUpdateAnimalSuccessfully() throws Exception {
    Animal animal = new Animal();
    animal.setId(1L);

    when(animalRepository.findById(1L)).thenReturn(Optional.of(animal));
    when(animalRepository.save(any(Animal.class))).thenReturn(animal);

    var updatedAnimal = animalService.updateAnimal(1L, "Black", null, "M", "2023-01-01", null);

    assertEquals('M', updatedAnimal.getGender());
    assertEquals("Black", updatedAnimal.getPrimaryColor());
    verify(animalRepository, times(1)).findById(1L);
    verify(animalRepository, times(1)).save(any(Animal.class));
  }

  @Test
  void shouldThrowException_whenUpdateAnimalFails() throws Exception {
    Animal animal = new Animal();
    animal.setId(1L);

    when(animalRepository.findById(1L)).thenReturn(Optional.of(animal));
    when(animalRepository.save(any(Animal.class))).thenThrow(
        new RuntimeException("Database error"));

    assertThrows(AnimalUpdateException.class, () -> animalService
        .updateAnimal(1L, "Black", null, "M", "2023-01-01", null));
  }

  @Test
  void shouldRemoveAnimalSuccessfully() throws AnimalNotFoundException {
    Animal animal = new Animal();
    animal.setId(1L);

    when(animalRepository.findById(1L)).thenReturn(Optional.of(animal));

    var removedAnimal = animalService.removeAnimal(1L, "Adopted");

    assertNotNull(removedAnimal);
    verify(animalRepository, times(1)).findById(1L);
    verify(animalRepository, times(1)).delete(animal);
    verify(notificationService, times(1)).sendRemoveAnimalMessage(animal, "Adopted");
  }

  @Test
  void shouldFindAnimalByVaccinationId_whenFound() {
    Animal animal = new Animal();
    animal.setId(1L);

    Long vaccinationId = 123L;
    when(animalRepository.findAnimalByVaccinationId(vaccinationId)).thenReturn(animal);

    Animal result = animalService.findAnimalByVaccinationId(vaccinationId);

    assertEquals(animal, result);
    verify(animalRepository).findAnimalByVaccinationId(vaccinationId);
  }

  @Test
  void shouldReturnNull_whenAnimalByVaccinationIdNotFound() {
    Long vaccinationId = 999L;
    when(animalRepository.findAnimalByVaccinationId(vaccinationId)).thenReturn(null);

    Animal result = animalService.findAnimalByVaccinationId(vaccinationId);

    assertNull(result);
    verify(animalRepository).findAnimalByVaccinationId(vaccinationId);
  }
}
