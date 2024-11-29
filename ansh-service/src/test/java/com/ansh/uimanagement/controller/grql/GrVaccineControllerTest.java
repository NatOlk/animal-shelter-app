package com.ansh.uimanagement.controller.grql;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.ansh.entity.animal.Vaccination;
import com.ansh.uimanagement.service.VaccinationService;
import com.ansh.uimanagement.service.exception.VaccinationCreationException;
import com.ansh.uimanagement.service.exception.VaccinationNotFoundException;
import com.ansh.uimanagement.service.exception.VaccinationUpdateException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.test.tester.GraphQlTester;

@GraphQlTest(controllers = GrVaccineController.class)
public class GrVaccineControllerTest {

  @Autowired
  private GraphQlTester graphQlTester;

  @MockBean
  private VaccinationService vaccinationService;

  private Vaccination vaccination;
  private Date vaccinationDate;

  @BeforeEach
  void setUp() throws Exception {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    vaccinationDate = sdf.parse("2024-11-29");

    vaccination = new Vaccination();
    vaccination.setId(1L);
    vaccination.setVaccine("Rabies");
    vaccination.setBatch("A123");
    vaccination.setVaccinationTime(vaccinationDate);
    vaccination.setComments("First dose");
    vaccination.setEmail("test@example.com");
  }

  @Test
  void shouldAddVaccination() throws VaccinationCreationException {
    when(vaccinationService.addVaccination(anyLong(), anyString(), anyString(),
        eq("2024-11-29"), anyString(), anyString()))
        .thenReturn(vaccination);

    String mutation = """
        mutation {
          addVaccination(animalId: 1, vaccine: "Rabies", batch: "A123", vaccinationTime: "2024-11-29", comments: "First dose", email: "test@example.com") {
            id
            vaccine
            batch
          }
        }
        """;

    graphQlTester.document(mutation)
        .execute()
        .path("addVaccination.id").entity(Long.class).isEqualTo(1L);
  }

  @Test
  void shouldUpdateVaccination() throws VaccinationNotFoundException, VaccinationUpdateException {
    when(vaccinationService.updateVaccination(anyLong(), anyString(), anyString(),
        eq("2024-11-29"), anyString(), anyString()))
        .thenReturn(vaccination);

    String mutation = """
        mutation {
          updateVaccination(id: 1, vaccine: "Rabies", batch: "A123", vaccinationTime: "2024-11-29", comments: "Updated comment", email: "test@example.com") {
            id
            vaccine
            batch
          }
        }
        """;

    graphQlTester.document(mutation)
        .execute()
        .path("updateVaccination.id").entity(Long.class).isEqualTo(1L);
  }

  @Test
  void shouldDeleteVaccination() throws VaccinationNotFoundException {
    when(vaccinationService.deleteVaccination(anyLong()))
        .thenReturn(vaccination);

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
  void shouldReturnAllVaccinations() {
    when(vaccinationService.getAllVaccinations())
        .thenReturn(Collections.singletonList(vaccination));

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
        .path("allVaccinations[0].id").entity(Long.class).isEqualTo(1L);
  }

  @Test
  void shouldReturnVaccinationCountById() {
    when(vaccinationService.vaccinationCountById(anyLong()))
        .thenReturn(5);

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
