package com.ansh.app.controller.grql;

import com.ansh.app.service.animal.impl.AnimalServiceImpl;
import com.ansh.app.service.exception.animal.AnimalCreationException;
import com.ansh.app.service.exception.animal.AnimalNotFoundException;
import com.ansh.app.service.exception.animal.AnimalUpdateException;
import com.ansh.dto.AnimalDTO;
import com.ansh.dto.AnimalInput;
import com.ansh.dto.UpdateAnimalInput;
import com.ansh.dto.VaccinationDTO;
import com.ansh.entity.animal.Animal;
import com.ansh.utils.AnimalMapper;
import graphql.GraphQLError;
import graphql.schema.DataFetchingEnvironment;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

@Controller
public class GrAnimalController {

  @Autowired
  private AnimalServiceImpl animalsService;

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
  @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
  public AnimalDTO addAnimal(@Argument AnimalInput animal) throws AnimalCreationException {
    return animalMapper.toDto(animalsService.addAnimal(animal));
  }

  @MutationMapping
  @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
  public AnimalDTO updateAnimal(@Argument UpdateAnimalInput animal)
      throws AnimalNotFoundException, AnimalUpdateException {
    return animalMapper.toDto(animalsService.updateAnimal(animal));
  }

  @MutationMapping
  @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
  public AnimalDTO deleteAnimal(@Argument Long id, @Argument String reason)
      throws AnimalNotFoundException {
    return animalMapper.toDto(animalsService.removeAnimal(id, reason));
  }

  @SchemaMapping(typeName = "VaccinationDTO", field = "animal")
  public AnimalDTO getAnimalByVaccinationId(VaccinationDTO vaccination) {
    return animalMapper.toDto(animalsService.findAnimalByVaccinationId(vaccination.getId()));
  }

  @GraphQlExceptionHandler
  public GraphQLError handle(@NonNull Throwable ex, @NonNull DataFetchingEnvironment environment) {
    return GraphQLError.newError()
        .errorType(ErrorType.BAD_REQUEST)
        .message(ex.getMessage())
        .path(environment.getExecutionStepInfo().getPath())
        .location(environment.getField().getSourceLocation()).build();
  }
}
