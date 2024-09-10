package com.example.gr.service;

import java.text.SimpleDateFormat;
import java.util.*;

import com.example.gr.service.exception.AnimalCreationException;
import com.example.gr.service.exception.AnimalNotFoundException;
import com.example.gr.service.exception.AnimalUpdateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.example.gr.jpa.AnimalRepository;
import com.example.gr.jpa.data.Animal;


@Service
public class AnimalService
{
	private static final Logger LOG = LoggerFactory.getLogger(AnimalService.class);
	private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

	@Autowired
	private AnimalRepository animalRepository;

	@Autowired
	private NotificationService notificationService;

	public List<Animal> getAllAnimals()
	{
		return animalRepository.findAll();
	}

	public Animal findById(Long id) throws AnimalNotFoundException {
		return animalRepository.findById(id)
				.orElseThrow(() -> new AnimalNotFoundException("Animal not found " + id));
	}

	public Animal addAnimal(@NonNull String name, @NonNull String  species,
							@NonNull String primary_color, String breed,
							String implant_chip_id, @NonNull String gender,
							String birth_date, String pattern) throws AnimalCreationException
	{
		try
		{
			Animal animal = new Animal();
			animal.setName(name);
			animal.setSpecies(species);
			animal.setBreed(breed);
			animal.setGender(gender.charAt(0));
			animal.setPattern(pattern);
			animal.setBirth_date(formatter.parse(birth_date));
			animal.setAdmission_date(new Date());
			animal.setPrimary_color(primary_color);
			animal.setImplant_chip_id(implant_chip_id);
			animalRepository.save(animal);

			notificationService.sendAddAnimalMessage(animal);
			return animal;
		} catch (Exception e)
		{
			throw new AnimalCreationException("Could not create animal: " + e.getMessage());
		}
	}

	public Animal updateAnimal(@NonNull Long id, String primary_color,
							   String breed, String gender,
							   String birth_date, String pattern)
			throws AnimalNotFoundException, AnimalUpdateException
	{
		Animal animal = animalRepository.findById(id)
				.orElseThrow(() -> new AnimalNotFoundException("Animal not found " + id));

        try
		{
			if (gender != null)
			{
				animal.setGender(gender.charAt(0));
			}
			if (birth_date != null)
			{
				animal.setBirth_date(formatter.parse(birth_date));
			}
			if (pattern != null)
			{
				animal.setPattern(pattern);
			}
			if (primary_color != null)
			{
				animal.setPrimary_color(primary_color);
			}
			if (breed != null)
			{
				animal.setBreed(breed);
			}
			animalRepository.save(animal);
        } catch (Exception e)
		{
            throw new AnimalUpdateException("Could not update animal:" + e.getMessage());
        }

		return animal;
	}

	public void removeAnimal(@NonNull Long id)
	{
		animalRepository.deleteById(id);
	}
}
