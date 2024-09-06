package com.example.gr.jpa.data;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class VaccinationKey implements Serializable
{
	@Column
	private String name;
	@Column
	private String species;
	@Column
	private String vaccine;
	@Column
	private String batch;

	public VaccinationKey() {
	}

	public VaccinationKey(String name, String species, String vaccine, String batch) {
		this.name = name;
		this.species = species;
		this.vaccine = vaccine;
		this.batch = batch;
	}
}
