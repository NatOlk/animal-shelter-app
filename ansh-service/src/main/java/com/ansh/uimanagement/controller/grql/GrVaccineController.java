package com.ansh.uimanagement.controller.grql;

import com.ansh.dto.AnimalDTO;
import com.ansh.dto.VaccinationDTO;
import com.ansh.uimanagement.service.VaccinationService;
import com.ansh.uimanagement.service.exception.VaccinationCreationException;
import com.ansh.uimanagement.service.exception.VaccinationNotFoundException;
import com.ansh.uimanagement.service.exception.VaccinationUpdateException;
import com.ansh.utils.VaccinationMapper;
import graphql.GraphQLError;
import graphql.schema.DataFetchingEnvironment;
import java.time.LocalDate;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class GrVaccineController {

  private static final Logger LOG = LoggerFactory.getLogger(GrVaccineController.class);

  @Autowired
  private VaccinationService vaccinationService;
  @Autowired
  private VaccinationMapper vaccinationMapper;

  @MutationMapping
  public VaccinationDTO updateVaccination(@Argument Long id, @Argument String vaccine,
      @Argument String batch, @Argument LocalDate vaccinationTime, @Argument String comments,
      @Argument String email) throws VaccinationNotFoundException, VaccinationUpdateException {
    return vaccinationMapper.toDto(
        vaccinationService.updateVaccination(id, vaccine, batch, vaccinationTime, comments,
            email));
  }

  @MutationMapping
  public VaccinationDTO deleteVaccination(@Argument Long id) throws VaccinationNotFoundException {
    return vaccinationMapper.toDto(vaccinationService.deleteVaccination(id));
  }

  @QueryMapping
  public List<VaccinationDTO> allVaccinations() {
    return vaccinationMapper.toDto(vaccinationService.getAllVaccinations());
  }

  @QueryMapping
  public List<VaccinationDTO> vaccinationByAnimalId(@Argument Long animalId) {
    return vaccinationMapper.toDto(vaccinationService.findByAnimalId(animalId));
  }

  @QueryMapping
  public int vaccinationCountById(@Argument Long id) {
    return vaccinationService.vaccinationCountById(id);
  }

  @MutationMapping
  public VaccinationDTO addVaccination(@Argument Long animalId, @Argument String vaccine,
      @Argument String batch, @Argument LocalDate vaccinationTime, @Argument String comments,
      @Argument String email) throws VaccinationCreationException {
    return vaccinationMapper.toDto(
        vaccinationService.addVaccination(animalId, vaccine, batch, vaccinationTime, comments,
            email));
  }

  @SchemaMapping(typeName = "Animal", field = "vaccinations")
  public List<VaccinationDTO> getVaccinations(AnimalDTO animal) {
    return vaccinationMapper.toDto(vaccinationService.findByAnimalId(animal.getId()));
  }

  @SchemaMapping(typeName = "Animal", field = "vaccinationCount")
  public int vaccinationCount(AnimalDTO animal) {
    return vaccinationService.vaccinationCountById(animal.getId());
  }

  @GraphQlExceptionHandler
  public GraphQLError handle(@NonNull Throwable ex, @NonNull DataFetchingEnvironment environment) {
    return GraphQLError.newError().errorType(ErrorType.BAD_REQUEST).message(ex.getMessage())
        .path(environment.getExecutionStepInfo().getPath())
        .location(environment.getField().getSourceLocation()).build();
  }
}
