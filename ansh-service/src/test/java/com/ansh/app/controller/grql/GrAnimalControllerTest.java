package com.ansh.app.controller.grql;

import com.ansh.DateScalarConfiguration;
import com.ansh.dto.AnimalDTO;
import com.ansh.entity.animal.Animal;
import com.ansh.app.service.animal.impl.AnimalServiceImpl;
import com.ansh.app.service.exception.animal.AnimalCreationException;
import com.ansh.app.service.exception.animal.AnimalNotFoundException;
import com.ansh.app.service.exception.animal.AnimalUpdateException;
import com.ansh.utils.AnimalMapper;
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
  private AnimalServiceImpl animalService;

  @MockBean
  private AnimalMapper animalMapper;

  @Test
  void allAnimals_shouldReturnListOfAnimals() {
    List<Animal> animals = List.of(
        mockAnimalToDto(1L, "Fido", "Dog"),
        mockAnimalToDto(2L, "Bella", "Dog")
    );

    List<AnimalDTO> animalDTOs = animals.stream()
        .map(animal -> {
          AnimalDTO dto = new AnimalDTO();
          dto.setId(animal.getId());
          dto.setName(animal.getName());
          dto.setSpecies(animal.getSpecies());
          return dto;
        }).toList();

    Mockito.when(animalMapper.toDto(animals)).thenReturn(animalDTOs);

    Mockito.when(animalService.getAllAnimals()).thenReturn(animals);

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
    Animal animal = mockAnimalToDto(1L, "Fido", "Dog");

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

    Animal animal = mockAnimalToDto(1L, "Fido", "Dog");

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
    Animal animal = mockAnimalToDto(1L, "Fido", "Dog");

    Mockito.when(animalService.updateAnimal(1L, "White", "Beagle", "Male",
            LocalDate.parse("2022-01-01"), "Striped", null))
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
    Animal animal = mockAnimalToDto(1L, "Fido", "Dog");

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

  private Animal mockAnimalToDto(Long id, String name, String species) {
    Animal animal = new Animal();
    animal.setId(id);
    animal.setName(name);

    AnimalDTO animalDTO = new AnimalDTO();
    animalDTO.setId(id);
    animalDTO.setName(name);
    animalDTO.setSpecies(species);

    Mockito.when(animalMapper.toDto(animal)).thenReturn(animalDTO);

    return animal;
  }
}
