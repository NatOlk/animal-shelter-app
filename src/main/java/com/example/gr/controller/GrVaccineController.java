package com.example.gr.controller;

import com.example.gr.jpa.data.Animal;
import com.example.gr.jpa.data.Vaccination;
import com.example.gr.service.VaccinationService;
import com.example.gr.service.exception.*;
import graphql.GraphQLError;
import graphql.schema.DataFetchingEnvironment;
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

import java.util.List;


@Controller
public class GrVaccineController
{
	private static final Logger LOG = LoggerFactory.getLogger(GrVaccineController.class);

	@Autowired
	private VaccinationService vaccinationService;

	@MutationMapping
	public Vaccination updateVaccination(@Argument String name, @Argument String species,
									@Argument String vaccine, @Argument String batch,
									@Argument String vaccination_time, @Argument String comments,
							        @Argument String email) throws VaccinationNotFoundException, VaccinationUpdateException {
		return vaccinationService.updateVaccination(name, species, vaccine, batch, vaccination_time,
				comments, email);
	}

	@MutationMapping
	public Vaccination deleteVaccination(@Argument String name, @Argument String species,
									@Argument String vaccine, @Argument String batch) throws VaccinationNotFoundException {
		//TODO fix it
		Vaccination vaccination = vaccinationService.findByKey(name, species, vaccine, batch)
				.orElseThrow(() -> new VaccinationNotFoundException("Vaccination not found for: " + name + ", " + species));
		vaccinationService.deleteVaccination(name, species, vaccine, batch);
		return vaccination;
	}

	@QueryMapping
	public List<Vaccination> allVaccinations()
	{
		return vaccinationService.getAllVaccinations();
	}

	@QueryMapping
	public List<Vaccination> vaccinationByNameAndSpecies(@Argument String name, @Argument String species)
	{
		return vaccinationService.findByNameAndSpecies(name, species);
	}


	@QueryMapping
	public int vaccinationCountByNameAndSpecies(@Argument String name, @Argument String species)
	{
		return vaccinationService.vaccinationCountByNameAndSpecies(name, species);
	}

	@MutationMapping
	public Vaccination addVaccination(@Argument String name, @Argument String species,
									  @Argument String vaccine, @Argument String batch,
									  @Argument String vaccination_time, @Argument String comments,
									  @Argument String email) throws VaccinationCreationException {
		return vaccinationService.addVaccination(name, species, vaccine, batch, vaccination_time, comments, email);
	}


	@SchemaMapping(typeName="Animal", field="vaccinations")
	public List<Vaccination> getVaccinations(Animal animal) {
		return vaccinationService.findByNameAndSpecies(animal.getName(), animal.getSpecies());
	}

	@GraphQlExceptionHandler
	public GraphQLError handle(@NonNull Throwable ex, @NonNull DataFetchingEnvironment environment){
		return GraphQLError
				.newError()
				.errorType(ErrorType.BAD_REQUEST)
				.message(ex.getMessage())
				.path(environment.getExecutionStepInfo().getPath())
				.location(environment.getField().getSourceLocation())
				.build();
	}
}
