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
	@OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Vaccination> vaccinations;
}
