package com.example.gr.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.example.gr.service.exception.AnimalCreationException;
import com.example.gr.service.exception.AnimalNotFoundException;
import com.example.gr.service.exception.AnimalUpdateException;
import graphql.GraphQLError;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;

import com.example.gr.jpa.data.Animal;
import com.example.gr.service.AnimalService;

@Controller
public class GrAnimalController
{
	@Autowired
	private AnimalService animalsService;

	@QueryMapping
	public List<Animal> allAnimals()
	{
		List<Animal> animals = animalsService.getAllAnimals();

		Collections.sort(animals, Comparator.comparing(animal -> animal.getId()));

		return animals;
	}

	@QueryMapping
	public Animal animalById(@Argument Long id) throws AnimalNotFoundException {
		return animalsService.findById(id);
	}

	@MutationMapping
	public Animal addAnimal(@Argument String name, @Argument String species, @Argument String primaryColor,
		  @Argument String breed, @Argument String implantChipId, @Argument String gender, @Argument String birthDate,
		  @Argument String pattern) throws AnimalCreationException {
        return animalsService.addAnimal(name, species, primaryColor, breed, implantChipId, gender, birthDate, pattern);
    }

	@MutationMapping
	public Animal updateAnimal(@Argument Long id, @Argument String primaryCcolor,
							   @Argument String breed, @Argument String gender,
							   @Argument String birthDate, @Argument String pattern) throws AnimalNotFoundException, AnimalUpdateException {
		return animalsService.updateAnimal(id, primaryCcolor, breed,  gender, birthDate, pattern);
	}

	@MutationMapping
	public Animal deleteAnimal(@Argument Long id) throws AnimalNotFoundException {
		Animal animal = animalsService.findById(id);
		animalsService.removeAnimal(id);
		return animal;
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
