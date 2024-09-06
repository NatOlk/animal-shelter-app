package com.example.gr.jpa.data;

import java.util.Date;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "animals", schema = "public")
@Data
public class Animal
{
	@EmbeddedId
	private AnimalKey animalKey;
	@Column
	private String primary_color;
	@Column
	private String implant_chip_id;
	@Column
	private String breed;
	@Column
	private char gender;
	@Column
	private Date birth_date;
	@Column
	private String pattern;
	@Column
	private Date admission_date;

	public String getName()
	{
		return animalKey.getName();
	}
	public String getSpecies()
	{
		return animalKey.getSpecies();
	}
}
