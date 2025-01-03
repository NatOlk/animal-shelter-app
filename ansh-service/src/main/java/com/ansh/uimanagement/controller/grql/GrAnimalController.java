package com.ansh.uimanagement.controller.grql;

import com.ansh.entity.animal.Animal;
import com.ansh.entity.animal.Vaccination;
import com.ansh.uimanagement.service.AnimalService;
import com.ansh.uimanagement.service.exception.AnimalCreationException;
import com.ansh.uimanagement.service.exception.AnimalNotFoundException;
import com.ansh.uimanagement.service.exception.AnimalUpdateException;
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

  @QueryMapping
  public List<Animal> allAnimals() {
    List<Animal> animals = animalsService.getAllAnimals();
    return animals;
  }

  @QueryMapping
  public Animal animalById(@Argument Long id) throws AnimalNotFoundException {
    return animalsService.findById(id);
  }

  @MutationMapping
  public Animal addAnimal(@Argument String name, @Argument String species,
      @Argument String primaryColor, @Argument String breed, @Argument String implantChipId,
      @Argument String gender, @Argument LocalDate birthDate, @Argument String pattern)
      throws AnimalCreationException {
    return animalsService.addAnimal(name, species, primaryColor, breed, implantChipId, gender,
        birthDate, pattern);
  }

  @MutationMapping
  public Animal updateAnimal(@Argument Long id, @Argument String primaryColor,
      @Argument String breed, @Argument String gender, @Argument LocalDate birthDate,
      @Argument String pattern) throws AnimalNotFoundException, AnimalUpdateException {
    return animalsService.updateAnimal(id, primaryColor, breed, gender, birthDate, pattern);
  }

  @MutationMapping
  public Animal deleteAnimal(@Argument Long id, @Argument String reason)
      throws AnimalNotFoundException {
    return animalsService.removeAnimal(id, reason);
  }

  @SchemaMapping(typeName = "Vaccination", field = "animal")
  public Animal getAnimalByVaccinationId(Vaccination vaccination) {
    return animalsService.findAnimalByVaccinationId(vaccination.getId());
  }

  @GraphQlExceptionHandler
  public GraphQLError handle(@NonNull Throwable ex, @NonNull DataFetchingEnvironment environment) {
    return GraphQLError.newError().errorType(ErrorType.BAD_REQUEST).message(ex.getMessage())
        .path(environment.getExecutionStepInfo().getPath())
        .location(environment.getField().getSourceLocation()).build();
  }
}
