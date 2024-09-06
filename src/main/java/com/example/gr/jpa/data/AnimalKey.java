package com.example.gr.jpa.data;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.*;
import lombok.Data;

@Embeddable
@Data
public class AnimalKey implements Serializable
{
	@Column
	private String name;
	@Column
	private String species;

	public AnimalKey()
	{
	}

	public AnimalKey(final String name, final String species)
	{
		this.name = name;
		this.species = species;
	}
}
