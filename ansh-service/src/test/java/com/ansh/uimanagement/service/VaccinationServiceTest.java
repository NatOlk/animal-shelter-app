package com.ansh.uimanagement.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ansh.entity.animal.Animal;
import com.ansh.entity.animal.Vaccination;
import com.ansh.notification.NotificationService;
import com.ansh.repository.AnimalRepository;
import com.ansh.repository.VaccinationRepository;
import com.ansh.uimanagement.service.exception.VaccinationCreationException;
import com.ansh.uimanagement.service.exception.VaccinationNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class VaccinationServiceTest {

  @Mock
  private VaccinationRepository vaccinationRepository;

  @Mock
  private AnimalRepository animalRepository;

  @Mock
  private NotificationService notificationService;

  @InjectMocks
  private VaccinationService vaccinationService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void shouldReturnAllVaccinations() {
    List<Vaccination> mockVaccinations = List.of(new Vaccination(), new Vaccination());
    when(vaccinationRepository.findAll()).thenReturn(mockVaccinations);

    List<Vaccination> result = vaccinationService.getAllVaccinations();

    assertNotNull(result);
    assertEquals(2, result.size());
    verify(vaccinationRepository, times(1)).findAll();
  }

  @Test
  void shouldAddVaccinationSuccessfully() throws Exception {
    Long animalId = 1L;
    String vaccine = "Rabies";
    String batch = "B123";
    LocalDate vaccinationTime = LocalDate.parse("2024-01-01");
    String comments = "First dose";
    String email = "vet@example.com";

    Animal mockAnimal = new Animal();
    mockAnimal.setId(animalId);

    when(animalRepository.findById(animalId)).thenReturn(Optional.of(mockAnimal));
    when(vaccinationRepository.save(any(Vaccination.class))).thenAnswer(
        invocation -> invocation.getArgument(0));

    Vaccination result = vaccinationService.addVaccination(animalId, vaccine, batch,
        vaccinationTime, comments, email);

    assertNotNull(result);
    assertEquals(vaccine, result.getVaccine());
    assertEquals(batch, result.getBatch());
    assertEquals(comments, result.getComments());
    assertEquals(email, result.getEmail());
    assertEquals(mockAnimal, result.getAnimal());
    assertEquals(vaccinationTime, result.getVaccinationTime());
    verify(vaccinationRepository, times(1)).save(any(Vaccination.class));
    verify(notificationService, times(1)).sendAddVaccinationMessage(result);
  }

  @Test
  void shouldThrowExceptionWhenAnimalNotFound() {
    Long animalId = 1L;

    when(animalRepository.findById(animalId)).thenReturn(Optional.empty());

    VaccinationCreationException exception = assertThrows(VaccinationCreationException.class, () ->
        vaccinationService.addVaccination(animalId, "Rabies", "B123",
            LocalDate.parse("2024-11-01"), "Comments","vet@example.com")
    );
    assertTrue(exception.getMessage().contains("Animal is not found 1"));
  }

  @Test
  void shouldDeleteVaccinationSuccessfully() throws VaccinationNotFoundException {
    Long vaccinationId = 1L;
    Vaccination mockVaccination = new Vaccination();
    mockVaccination.setId(vaccinationId);

    when(vaccinationRepository.findById(vaccinationId)).thenReturn(Optional.of(mockVaccination));

    Vaccination result = vaccinationService.deleteVaccination(vaccinationId);

    assertNotNull(result);
    assertEquals(vaccinationId, result.getId());
    verify(vaccinationRepository, times(1)).delete(mockVaccination);
    verify(notificationService, times(1)).sendRemoveVaccinationMessage(mockVaccination);
  }

  @Test
  void shouldThrowExceptionWhenVaccinationNotFound() {
    Long vaccinationId = 1L;

    when(vaccinationRepository.findById(vaccinationId)).thenReturn(Optional.empty());

    VaccinationNotFoundException exception = assertThrows(VaccinationNotFoundException.class, () ->
        vaccinationService.deleteVaccination(vaccinationId)
    );
    assertTrue(exception.getMessage().contains("Vaccination not found"));
  }
}
