package com.example.gr.service;

import java.text.SimpleDateFormat;
import java.util.*;

import com.example.gr.jpa.AnimalRepository;
import com.example.gr.jpa.data.Animal;
import com.example.gr.service.exception.VaccinationCreationException;
import com.example.gr.service.exception.VaccinationNotFoundException;
import com.example.gr.service.exception.VaccinationUpdateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.example.gr.jpa.VaccinationRepository;
import com.example.gr.jpa.data.Vaccination;


@Service
public class VaccinationService
{
	private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

	@Autowired
	private VaccinationRepository vaccinationRepository;

	@Autowired
	private AnimalRepository animalRepository;

	@Autowired
	private NotificationService notificationService;

	public List<Vaccination> getAllVaccinations()
	{
		return vaccinationRepository.findAll();
	}

	public List<Vaccination> findByAnimalId(Long animalId)
	{
		return vaccinationRepository.findByAnimalId(animalId);
	}

	public int vaccinationCountById(Long id)
	{
		return vaccinationRepository.findVaccinationCountByAnimalId(id);
	}

	public Optional<Vaccination> findByKey(Long id)
	{
		return vaccinationRepository.findById(id);
	}

	public Vaccination addVaccination(@NonNull Long animalId, @NonNull String vaccine,
									  @NonNull String batch, @NonNull String vaccination_time,
									  String comments, @NonNull String email) throws VaccinationCreationException {
		try {
			Animal animal = animalRepository.findById(animalId).orElse(null);
			if (animal == null) throw new VaccinationCreationException("Animal is not found " + animalId);

			Vaccination vaccination = new Vaccination();
			vaccination.setVaccine(vaccine);
			vaccination.setBatch(batch);
			vaccination.setVaccination_time(formatter.parse(vaccination_time));
			vaccination.setComments(comments);
			vaccination.setEmail(email);
			vaccination.setAnimal(animal);
			//	vaccination.setVaccination_time(new Date());

			notificationService.sendAddVaccinationMessage(vaccination);
			vaccinationRepository.save(vaccination);


			return vaccination;
		} catch(Exception e)
		{
			throw new VaccinationCreationException("Error during add vaccination : ");
		}
	}

	public Vaccination updateVaccination(@NonNull Long id, String vaccine,
										 String batch, String vaccination_time,
										 String comments, String email)
			throws VaccinationNotFoundException, VaccinationUpdateException
	{
		//TODO: fix exception
		Vaccination vaccination = vaccinationRepository.findById(id)
				.orElseThrow(() -> new VaccinationNotFoundException("Vaccination not found " + id));

		try
		{
			if(vaccine != null)
			{
				vaccination.setVaccine(vaccine);
			}
			if (batch != null)
			{
				vaccination.setBatch(batch);
			}

			if (vaccination_time != null)
			{
				vaccination.setVaccination_time(formatter.parse(vaccination_time));
			}
			if (comments != null)
			{
				vaccination.setComments(comments);
			}
			if (email != null)
			{
				vaccination.setEmail(email);
			}
			vaccinationRepository.save(vaccination);
		} catch (Exception e)
		{
			throw new VaccinationUpdateException("Could not update animal:" + e.getMessage());
		}

		return vaccination;
	}

	public void deleteVaccination(@NonNull Long id)
	{
		vaccinationRepository.deleteById(id);
	}
}
