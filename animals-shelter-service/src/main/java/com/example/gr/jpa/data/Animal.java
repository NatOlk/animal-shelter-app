package com.example.gr.jpa.data;

import java.util.Date;
import java.util.List;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "animals", schema = "public")
@Data
public class Animal
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column
	private String name;
	@Column
	private String species;
	@Column
	private String primaryColor;
	@Column
	private String implantChipId;
	@Column
	private String breed;
	@Column
	private char gender;
	@Column
	private Date birthDate;
	@Column
	private String pattern;
	@Column
	private Date admissionDate;
	@OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Vaccination> vaccinations;
}
