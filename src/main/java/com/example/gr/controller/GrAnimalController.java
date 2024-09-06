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
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
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

		Collections.sort(animals, Comparator.comparing(animal -> animal.getAnimalKey().getName()));

		return animals;
	}

	@QueryMapping
	public Animal animalByNameAndSpecies(@Argument String name, @Argument String species) throws AnimalNotFoundException {
		return animalsService.findByNameAndSpecies(name, species);
	}

	@MutationMapping
	public Animal addAnimal(@Argument String name, @Argument String species, @Argument String primary_color,
		  @Argument String breed, @Argument String implant_chip_id, @Argument String gender, @Argument String birth_date,
		  @Argument String pattern) throws AnimalCreationException {
        return animalsService.addAnimal(name, species, primary_color, breed, implant_chip_id, gender, birth_date, pattern);
    }

	@MutationMapping
	public Animal updateAnimal(@Argument String name, @Argument String species, @Argument String primary_color,
							@Argument String breed, @Argument String gender, @Argument String birth_date,
							@Argument String pattern) throws AnimalNotFoundException, AnimalUpdateException {
		return animalsService.updateAnimal(name, species, primary_color, breed,  gender, birth_date, pattern);
	}

	@MutationMapping
	public Animal deleteAnimal(@Argument String name, @Argument String species) throws AnimalNotFoundException {
		Animal animal = animalsService.findByNameAndSpecies(name, species);
		animalsService.removeAnimal(name, species);
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
