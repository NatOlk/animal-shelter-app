package com.example.gr.service;

import java.text.SimpleDateFormat;
import java.util.*;

import com.example.gr.kafka.AnimalInfoProducer;
import com.example.gr.service.exception.VaccinationCreationException;
import com.example.gr.service.exception.VaccinationNotFoundException;
import com.example.gr.service.exception.VaccinationUpdateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.example.gr.jpa.VaccinationRepository;
import com.example.gr.jpa.data.Vaccination;
import com.example.gr.jpa.data.VaccinationKey;

import static com.example.gr.kafka.SubscriptionMessages.ADD_ANIMAL_MSG;
import static com.example.gr.kafka.SubscriptionMessages.ADD_VACCINE_MSG;


@Service
public class VaccinationService
{
	private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

	@Autowired
	private VaccinationRepository vaccinationRepository;

	@Autowired
	private NotificationService notificationService;

	public List<Vaccination> getAllVaccinations()
	{
		return vaccinationRepository.findAll();
	}

	public List<Vaccination> findByNameAndSpecies(String name, String species)
	{
		return vaccinationRepository.findByVaccinationKeyNameAndVaccinationKeySpecies(name, species);
	}

	public int vaccinationCountByNameAndSpecies(String name, String species)
	{
		return vaccinationRepository.findVaccinationCountByNameAndSpecies(name, species);
	}

	public Optional<Vaccination> findByKey(String name, String species, String vaccine, String batch)
	{
		return vaccinationRepository.findById(new VaccinationKey(name, species, vaccine, batch));
	}

	public Vaccination addVaccination(@NonNull String name, @NonNull String species,
									  @NonNull String vaccine, @NonNull String batch,
									  @NonNull String vaccination_time,
									  String comments, @NonNull String email) throws VaccinationCreationException {
		try {
			Vaccination vaccination = new Vaccination();
			vaccination.setVaccinationKey(new VaccinationKey(name, species, vaccine, batch));
			vaccination.setVaccination_time(formatter.parse(vaccination_time));
			vaccination.setComments(comments);
			vaccination.setEmail(email);
			//	vaccination.setVaccination_time(new Date());

			notificationService.sendAddVaccinationMessage(vaccination);
			vaccinationRepository.save(vaccination);


			return vaccination;
		} catch(Exception e)
		{
			throw new VaccinationCreationException("Error during add vaccination : ");
		}
	}

	public Vaccination updateVaccination(@NonNull String name, @NonNull String species,
									@NonNull String vaccine, @NonNull String batch,
									String vaccination_time, String comments, String email)
			throws VaccinationNotFoundException, VaccinationUpdateException
	{
		//TODO: fix exception
		Vaccination vaccination = vaccinationRepository.findById(new VaccinationKey(name, species, vaccine, batch))
				.orElseThrow(() -> new VaccinationNotFoundException("Animal not found " + name));

		try
		{
			vaccination.setVaccine(vaccine);
			vaccination.setBatch(batch);

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

	public void deleteVaccination(@NonNull String name, @NonNull String species,
								  @NonNull String vaccine, @NonNull String batch)
	{
		vaccinationRepository.deleteById(new VaccinationKey(name, species, vaccine, batch));
	}
}
