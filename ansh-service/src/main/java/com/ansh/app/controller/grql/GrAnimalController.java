package com.ansh.app.controller.grql;

import com.ansh.dto.AnimalDTO;
import com.ansh.dto.VaccinationDTO;
import com.ansh.entity.animal.Animal;
import com.ansh.app.service.AnimalService;
import com.ansh.app.service.exception.AnimalCreationException;
import com.ansh.app.service.exception.AnimalNotFoundException;
import com.ansh.app.service.exception.AnimalUpdateException;
import com.ansh.utils.AnimalMapper;
import graphql.GraphQLError;
import graphql.schema.DataFetchingEnvironment;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;

@Controller
public class GrAnimalController {

  @Autowired
  private AnimalService animalsService;

  @Autowired
  private AnimalMapper animalMapper;

  @QueryMapping
  public List<AnimalDTO> allAnimals() {
    List<Animal> animals = animalsService.getAllAnimals();
      return animalMapper.toDto(animals);
  }

  @QueryMapping
  public AnimalDTO animalById(@Argument Long id) throws AnimalNotFoundException {
    return animalMapper.toDto(animalsService.findById(id));
  }

  @MutationMapping
  public AnimalDTO addAnimal(@Argument String name, @Argument String species,
      @Argument String primaryColor, @Argument String breed, @Argument String implantChipId,
      @Argument String gender, @Argument LocalDate birthDate, @Argument String pattern)
      throws AnimalCreationException {
    return animalMapper.toDto(
        animalsService.addAnimal(name, species, primaryColor, breed, implantChipId, gender,
            birthDate, pattern));
  }

  @MutationMapping
  public AnimalDTO updateAnimal(@Argument Long id, @Argument String primaryColor,
      @Argument String breed, @Argument String gender, @Argument LocalDate birthDate,
      @Argument String pattern) throws AnimalNotFoundException, AnimalUpdateException {
    return animalMapper.toDto(
        animalsService.updateAnimal(id, primaryColor, breed, gender, birthDate, pattern));
  }

  @MutationMapping
  public AnimalDTO deleteAnimal(@Argument Long id, @Argument String reason)
      throws AnimalNotFoundException {
    return animalMapper.toDto(animalsService.removeAnimal(id, reason));
  }

  @SchemaMapping(typeName = "Vaccination", field = "animal")
  public AnimalDTO getAnimalByVaccinationId(VaccinationDTO vaccination) {
    return animalMapper.toDto(animalsService.findAnimalByVaccinationId(vaccination.getId()));
  }

  @GraphQlExceptionHandler
  public GraphQLError handle(@NonNull Throwable ex, @NonNull DataFetchingEnvironment environment) {
    return GraphQLError.newError().errorType(ErrorType.BAD_REQUEST).message(ex.getMessage())
        .path(environment.getExecutionStepInfo().getPath())
        .location(environment.getField().getSourceLocation()).build();
  }
}
