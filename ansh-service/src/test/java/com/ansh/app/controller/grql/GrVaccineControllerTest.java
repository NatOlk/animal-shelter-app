package com.ansh.app.controller.grql;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.ansh.DateScalarConfiguration;
import com.ansh.app.service.animal.impl.VaccinationServiceImpl;
import com.ansh.app.service.exception.animal.VaccinationCreationException;
import com.ansh.app.service.exception.animal.VaccinationNotFoundException;
import com.ansh.app.service.exception.animal.VaccinationUpdateException;
import com.ansh.dto.UpdateVaccinationInput;
import com.ansh.dto.VaccinationDTO;
import com.ansh.dto.VaccinationInput;
import com.ansh.entity.animal.Vaccination;
import com.ansh.utils.VaccinationMapper;
import java.time.LocalDate;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.test.tester.GraphQlTester;

@GraphQlTest(controllers = GrVaccineController.class)
@Import(DateScalarConfiguration.class)
class GrVaccineControllerTest {

  @Autowired
  private GraphQlTester graphQlTester;

  @MockBean
  private VaccinationServiceImpl vaccinationService;

  @MockBean
  private VaccinationMapper vaccinationMapper;

  private Vaccination vaccination;

  @BeforeEach
  void setUp() {
    LocalDate vaccinationDate = LocalDate.parse("2024-11-29");

    vaccination = new Vaccination();
    vaccination.setId(1L);
    vaccination.setVaccine("Rabies");
    vaccination.setBatch("A123");
    vaccination.setVaccinationTime(vaccinationDate);
    vaccination.setComments("First dose");
    vaccination.setEmail("test@example.com");

    VaccinationDTO vaccinationDTO = new VaccinationDTO();
    vaccinationDTO.setId(1L);
    vaccinationDTO.setVaccine("Rabies");
    vaccinationDTO.setBatch("A123");
    vaccinationDTO.setVaccinationTime(vaccinationDate);
    vaccinationDTO.setComments("First dose");
    vaccinationDTO.setEmail("test@example.com");

    Mockito.when(vaccinationMapper.toDto(vaccination)).thenReturn(vaccinationDTO);

    Mockito.when(vaccinationMapper.toDto(Collections.singletonList(vaccination)))
        .thenReturn(Collections.singletonList(vaccinationDTO));
  }

  @Test
  void addVaccination_shouldReturnAddedVaccination() throws VaccinationCreationException {

    VaccinationInput vaccinationInput = new VaccinationInput(1L, "Rabies", "A123",
        "test@example.com", LocalDate.parse("2024-11-29"), "First dose");
    when(vaccinationService.addVaccination(vaccinationInput)).thenReturn(vaccination);

    String mutation = """
        mutation {
          addVaccination(vaccination: { 
            animalId: 1, 
            vaccine: "Rabies", 
            batch: "A123", 
            vaccinationTime: "2024-11-29", 
            comments: "First dose", 
            email: "test@example.com" 
          }) {  
            id
            vaccine
            batch
          }
        }
        """;

    graphQlTester.document(mutation)
        .execute()
        .path("addVaccination.id").entity(Long.class).isEqualTo(1L)
        .path("addVaccination.vaccine").entity(String.class).isEqualTo("Rabies")
        .path("addVaccination.batch").entity(String.class).isEqualTo("A123");
  }

  @Test
  void updateVaccination_shouldUpdateVaccine()
      throws VaccinationNotFoundException, VaccinationUpdateException {
    UpdateVaccinationInput vaccinationInput = new UpdateVaccinationInput(1L,"Rabies",
        "A123","test@example.com", LocalDate.parse("2024-11-29"), "First dose");

    when(vaccinationService.updateVaccination(vaccinationInput)).thenReturn(vaccination);

    String mutation = """
        mutation {
          updateVaccination(vaccination: { 
            id: 1, 
            vaccine: "Rabies", 
            batch: "A123", 
            vaccinationTime: "2024-11-29", 
            comments: "First dose", 
            email: "test@example.com" 
          }) {
            id
            vaccine
            batch
            comments
          }
        }
        """;

    graphQlTester.document(mutation)
        .execute()
        .path("updateVaccination.id").entity(Long.class).isEqualTo(1L)
        .path("updateVaccination.vaccine").entity(String.class).isEqualTo("Rabies")
        .path("updateVaccination.batch").entity(String.class).isEqualTo("A123")
        .path("updateVaccination.comments").entity(String.class).isEqualTo("First dose");
  }

  @Test
  void shouldDeleteVaccination() throws VaccinationNotFoundException {
    when(vaccinationService.deleteVaccination(anyLong())).thenReturn(vaccination);

    String mutation = """
        mutation {
          deleteVaccination(id: 1) {
            id
          }
        }
        """;

    graphQlTester.document(mutation)
        .execute()
        .path("deleteVaccination.id").entity(Long.class).isEqualTo(1L);
  }

  @Test
  void allVaccinations_shouldReturnList() {
    when(vaccinationService.getAllVaccinations()).thenReturn(
        Collections.singletonList(vaccination));

    String query = """
        query {
          allVaccinations {
            id
            vaccine
            batch
          }
        }
        """;

    graphQlTester.document(query)
        .execute()
        .path("allVaccinations[0].id").entity(Long.class).isEqualTo(1L)
        .path("allVaccinations[0].vaccine").entity(String.class).isEqualTo("Rabies")
        .path("allVaccinations[0].batch").entity(String.class).isEqualTo("A123");
  }

  @Test
  void vaccinationCountById_shouldReturnCount() {
    when(vaccinationService.vaccinationCountById(anyLong())).thenReturn(5);

    String query = """
        query {
          vaccinationCountById(id: 1)
        }
        """;

    graphQlTester.document(query)
        .execute()
        .path("vaccinationCountById").entity(Integer.class).isEqualTo(5);
  }
}
