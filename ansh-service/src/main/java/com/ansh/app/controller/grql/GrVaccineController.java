package com.ansh.app.controller.grql;

import com.ansh.app.service.animal.impl.VaccinationServiceImpl;
import com.ansh.app.service.exception.animal.VaccinationCreationException;
import com.ansh.app.service.exception.animal.VaccinationNotFoundException;
import com.ansh.app.service.exception.animal.VaccinationUpdateException;
import com.ansh.dto.AnimalDTO;
import com.ansh.dto.UpdateVaccinationInput;
import com.ansh.dto.VaccinationDTO;
import com.ansh.dto.VaccinationInput;
import com.ansh.utils.VaccinationMapper;
import graphql.GraphQLError;
import graphql.schema.DataFetchingEnvironment;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

@Controller
public class GrVaccineController {

  private static final Logger LOG = LoggerFactory.getLogger(GrVaccineController.class);

  @Autowired
  private VaccinationServiceImpl vaccinationService;
  @Autowired
  private VaccinationMapper vaccinationMapper;

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
  @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
  public VaccinationDTO updateVaccination(@Argument UpdateVaccinationInput vaccination)
      throws VaccinationNotFoundException, VaccinationUpdateException {
    return vaccinationMapper.toDto(vaccinationService.updateVaccination(vaccination));
  }

  @MutationMapping
  @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
  public VaccinationDTO deleteVaccination(@Argument Long id) throws VaccinationNotFoundException {
    return vaccinationMapper.toDto(vaccinationService.deleteVaccination(id));
  }

  @MutationMapping
  @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
  public VaccinationDTO addVaccination(@Argument VaccinationInput vaccination)
      throws VaccinationCreationException {
    return vaccinationMapper.toDto(vaccinationService.addVaccination(vaccination));
  }

  @SchemaMapping(typeName = "AnimalDTO", field = "vaccinations")
  public List<VaccinationDTO> getVaccinations(AnimalDTO animal) {
    return vaccinationMapper.toDto(vaccinationService.findByAnimalId(animal.getId()));
  }

  @SchemaMapping(typeName = "AnimalDTO", field = "vaccinationCount")
  public int vaccinationCount(AnimalDTO animal) {
    return vaccinationService.vaccinationCountById(animal.getId());
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
