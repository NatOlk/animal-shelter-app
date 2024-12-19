package com.ansh.entity.animal;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import java.util.Date;
import java.util.List;
import lombok.Data;

@Entity
@Table(name = "animals", schema = "public")
@Data
public class Animal {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  @Column
  private String name;
  @Column
  private String species;
  @Column
  private String primaryColor;
  @Column(unique = true, nullable = true)
  @Pattern(regexp = "\\d{8}-\\d{8}-\\d{8}", message = "Value must match the pattern 11111111-11111111-1111")
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
  @JsonManagedReference
  @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Vaccination> vaccinations;
}
