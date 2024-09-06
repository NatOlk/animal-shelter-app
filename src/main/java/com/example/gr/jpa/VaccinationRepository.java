package com.example.gr.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import com.example.gr.jpa.data.Vaccination;
import com.example.gr.jpa.data.VaccinationKey;


public interface VaccinationRepository extends JpaRepository<Vaccination, VaccinationKey>
{
	@Query(name = "select * from Vaccination v where v.vaccinationKey.name=:name and v.vaccinationKey.species=:species")
	List<Vaccination> findByVaccinationKeyNameAndVaccinationKeySpecies(@NonNull String name, @NonNull String species);

	@Query("select count(v) from Vaccination v where v.animal.name = :name and v.animal.species = :species")
	int findVaccinationCountByNameAndSpecies(@NonNull String name, @NonNull String species);

}
