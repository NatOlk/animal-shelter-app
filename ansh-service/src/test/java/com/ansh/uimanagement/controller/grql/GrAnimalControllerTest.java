package com.ansh.uimanagement.controller.grql;

import com.ansh.DateScalarConfiguration;
import com.ansh.entity.animal.Animal;
import com.ansh.uimanagement.service.AnimalService;
import com.ansh.uimanagement.service.exception.AnimalCreationException;
import com.ansh.uimanagement.service.exception.AnimalNotFoundException;
import com.ansh.uimanagement.service.exception.AnimalUpdateException;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.graphql.test.tester.GraphQlTester.Response;

@GraphQlTest(controllers = GrAnimalController.class)
@Import(DateScalarConfiguration.class)
class GrAnimalControllerTest {

  @Autowired
  private GraphQlTester graphQlTester;

  @MockBean
  private AnimalService animalService;

  @Test
  void allAnimals_shouldReturnListOfAnimals() {
    Animal animal1 = new Animal();
    animal1.setId(1L);
    animal1.setName("Fido");

    Animal animal2 = new Animal();
    animal2.setId(2L);
    animal2.setName("Bella");

    Mockito.when(animalService.getAllAnimals()).thenReturn(List.of(animal1, animal2));

    String query = """
            query {
                allAnimals {
                    id
                    name
                }
            }
        """;

    Response response = graphQlTester.document(query)
        .execute();

    response.path("allAnimals[0].id").entity(Long.class).isEqualTo(1L);
    response.path("allAnimals[0].name").entity(String.class).isEqualTo("Fido");
    response.path("allAnimals[1].id").entity(Long.class).isEqualTo(2L);
    response.path("allAnimals[1].name").entity(String.class).isEqualTo("Bella");
  }

  @Test
  void animalById_shouldReturnAnimal() throws AnimalNotFoundException {
    Animal animal = new Animal();
    animal.setId(1L);
    animal.setName("Fido");

    Mockito.when(animalService.findById(1L)).thenReturn(animal);

    String query = """
            query {
                animalById(id: 1) {
                    id
                    name
                }
            }
        """;

    Response response = graphQlTester.document(query)
        .execute();

    response.path("animalById.id").entity(Long.class).isEqualTo(1L);
    response.path("animalById.name").entity(String.class).isEqualTo("Fido");
  }

  @Test
  void addAnimal_shouldReturnAddedAnimal() throws AnimalCreationException {
    Animal animal = new Animal();
    animal.setId(1L);
    animal.setName("Fido");

    Mockito.when(
            animalService.addAnimal("Fido", "Dog", "Brown", "Labrador",
                "12345", "Male", LocalDate.parse("2022-01-01"), "Spotted"))
        .thenReturn(animal);

    String query = """
            mutation {
                addAnimal(name: "Fido", species: "Dog", primaryColor: "Brown", breed: "Labrador", implantChipId: "12345", 
                gender: "Male", birthDate: "2022-01-01", pattern: "Spotted") {
                    id
                    name
                }
            }
        """;

    Response response = graphQlTester.document(query)
        .execute();

    response.path("addAnimal.id").entity(Long.class).isEqualTo(1L);
    response.path("addAnimal.name").entity(String.class).isEqualTo("Fido");
  }

  @Test
  void updateAnimal_shouldReturnUpdatedAnimal()
      throws AnimalUpdateException, AnimalNotFoundException {
    Animal animal = new Animal();
    animal.setId(1L);
    animal.setName("Fido");

    Mockito.when(animalService.updateAnimal(1L, "White", "Beagle", "Male",
            LocalDate.parse("2022-01-01"), "Striped"))
        .thenReturn(animal);

    String query = """
            mutation {
                updateAnimal(id: 1, primaryColor: "White", breed: "Beagle", gender: "Male", birthDate: "2022-01-01", pattern: "Striped") {
                    id
                    name
                }
            }
        """;

    Response response = graphQlTester.document(query)
        .execute();

    response.path("updateAnimal.id").entity(Long.class).isEqualTo(1L);
    response.path("updateAnimal.name").entity(String.class).isEqualTo("Fido");
  }

  @Test
  void deleteAnimal_shouldReturnDeletedAnimal() throws AnimalNotFoundException {
    Animal animal = new Animal();
    animal.setId(1L);
    animal.setName("Fido");

    Mockito.when(animalService.removeAnimal(1L, "Wrong information"))
        .thenReturn(animal);

    String query = """
            mutation {
                deleteAnimal(id: 1, reason: "Wrong information") {
                    id
                    name
                }
            }
        """;

    Response response = graphQlTester.document(query)
        .execute();

    response.path("deleteAnimal.id").entity(Long.class).isEqualTo(1L);
    response.path("deleteAnimal.name").entity(String.class).isEqualTo("Fido");
  }
}
