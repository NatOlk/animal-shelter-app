package com.example.gr.jpa.data;

import java.util.Date;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "vaccinations", schema = "public")
@Data
public class Vaccination
{
	@EmbeddedId
	private VaccinationKey vaccinationKey;
	@Column
	private String email;
	@Column
	private Date vaccination_time;
	@Column
	private String comments;


	public String getVaccine() {
		return vaccinationKey.getVaccine();
	}

	public void setVaccine(String vaccine) {
		vaccinationKey.setVaccine(vaccine);
	}

	public String getBatch() {
		return vaccinationKey.getBatch();
	}

	public void setBatch(String batch) {
		vaccinationKey.setBatch(batch);
	}

	public String getName()
	{
		return vaccinationKey.getName();
	}
	public String getSpecies()
	{
		return vaccinationKey.getSpecies();
	}

	public void setName(String name)
	{
		vaccinationKey.setName(name);
	}
	public void setSpecies(String species)
	{ vaccinationKey.setSpecies(species);
	}

}
