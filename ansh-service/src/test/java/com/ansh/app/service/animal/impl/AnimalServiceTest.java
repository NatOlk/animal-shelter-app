package com.ansh.app.service.animal.impl;

import static graphql.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ansh.app.service.exception.animal.AnimalCreationException;
import com.ansh.app.service.exception.animal.AnimalNotFoundException;
import com.ansh.app.service.exception.animal.AnimalUpdateException;
import com.ansh.app.service.notification.animal.AnimalInfoNotificationService;
import com.ansh.dto.AnimalInput;
import com.ansh.dto.UpdateAnimalInput;
import com.ansh.entity.animal.Animal;
import com.ansh.repository.AnimalRepository;
import java.time.LocalDate;
import java.util.Arrays;
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
  private AnimalInfoNotificationService animalInfoNotificationService;

  @InjectMocks
  private AnimalServiceImpl animalService;

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
  void shouldFindAnimalById_whenExists() {
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
    AnimalInput animalInput = new AnimalInput(
        "Bella", "Dog", "Brown", "Labrador",
        "12345", "Female", LocalDate.parse("2022-01-01"), "Spotted");

    var addedAnimal = animalService.addAnimal(animalInput);

    assertNotNull(addedAnimal);
    assertEquals("Bella", addedAnimal.getName());
    verify(animalRepository, times(1)).save(any(Animal.class));
    verify(animalInfoNotificationService, times(1)).sendAddAnimalMessage(any(Animal.class));
  }

  @Test
  void shouldThrowException_whenAddAnimalFails() {
    when(animalRepository.save(any(Animal.class))).thenThrow(
        new RuntimeException("Database error"));
    AnimalInput animalInput = new AnimalInput(
        "Bella", "Dog", "Brown", "Labrador",
        "12345", "Female", LocalDate.parse("2022-01-01"), "Spotted");
    assertThrows(AnimalCreationException.class, () -> animalService.addAnimal(animalInput));

    verify(animalRepository, times(1)).save(any(Animal.class));
    verify(animalInfoNotificationService, never()).sendAddAnimalMessage(any(Animal.class));
  }

  @Test
  void shouldUpdateAnimalSuccessfully() throws Exception {
    Animal animal = new Animal();
    animal.setId(1L);

    when(animalRepository.findById(1L)).thenReturn(Optional.of(animal));
    when(animalRepository.save(any(Animal.class))).thenReturn(animal);

    UpdateAnimalInput updateAnimalInput = new UpdateAnimalInput(1L, "Black",
        null, "Male", LocalDate.parse("2022-01-01"), null, null);
    var updatedAnimal = animalService.updateAnimal(updateAnimalInput);

    assertEquals('M', updatedAnimal.getGender());
    assertEquals("Black", updatedAnimal.getPrimaryColor());
    verify(animalRepository, times(1)).findById(1L);
    verify(animalRepository, times(1)).save(any(Animal.class));
  }

  void shouldUpdateAnimalImageSuccessfully() throws Exception {
    Animal animal = new Animal();
    animal.setId(1L);

    when(animalRepository.findById(1L)).thenReturn(Optional.of(animal));
    when(animalRepository.save(any(Animal.class))).thenReturn(animal);
    UpdateAnimalInput updateAnimalInput = new UpdateAnimalInput(1L, "Black",
        null, "Male", LocalDate.parse("2022-01-01"), null, "newPath");
    var updatedAnimal = animalService.updateAnimal(updateAnimalInput);

    assertEquals('M', updatedAnimal.getGender());
    assertEquals("Black", updatedAnimal.getPrimaryColor());
    assertEquals("newPath", updatedAnimal.getPhotoImgPath());
    verify(animalRepository, times(1)).findById(1L);
    verify(animalRepository, times(1)).save(any(Animal.class));
  }

  @Test
  void shouldThrowException_whenUpdateAnimalFails() {
    Animal animal = new Animal();
    animal.setId(1L);

    when(animalRepository.findById(1L)).thenReturn(Optional.of(animal));
    when(animalRepository.save(any(Animal.class))).thenThrow(
        new RuntimeException("Database error"));

    UpdateAnimalInput updateAnimalInput = new UpdateAnimalInput(1L, "Black",
        null, "Male", LocalDate.parse("2023-01-01"), null, null);
    assertThrows(AnimalUpdateException.class, () -> animalService
        .updateAnimal(updateAnimalInput));
  }

  @Test
  void shouldRemoveAnimalSuccessfully() {
    Animal animal = new Animal();
    animal.setId(1L);

    when(animalRepository.findById(1L)).thenReturn(Optional.of(animal));

    var removedAnimal = animalService.removeAnimal(1L, "Adopted");

    assertNotNull(removedAnimal);
    verify(animalRepository, times(1)).findById(1L);
    verify(animalRepository, times(1)).delete(animal);
    verify(animalInfoNotificationService, times(1))
        .sendRemoveAnimalMessage(animal, "Adopted");
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
