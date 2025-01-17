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
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "animals", schema = "public", uniqueConstraints = {
    @UniqueConstraint(name = "unique_animal_attributes",
        columnNames = {"name", "species", "breed", "gender"})
})
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Animal {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  @Column
  @EqualsAndHashCode.Include
  private String name;
  @Column
  @EqualsAndHashCode.Include
  private String species;
  @Column
  private String primaryColor;
  @Column(unique = true, nullable = true)
  @Pattern(regexp = "\\d{8}-\\d{8}-\\d{8}", message = "Value must match the pattern 11111111-11111111-1111")
  private String implantChipId;
  @Column
  @EqualsAndHashCode.Include
  private String breed;
  @Column
  @EqualsAndHashCode.Include
  private char gender;
  @Column
  @EqualsAndHashCode.Include
  private LocalDate birthDate;
  @Column
  private String pattern;
  @Column
  private LocalDate admissionDate;
  @JsonManagedReference
  @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Vaccination> vaccinations;

  public void setImplantChipId(String implantChipId) {
    if (this.implantChipId != null) {
      throw new UnsupportedOperationException("Chip ID cannot be changed once it is set.");
    }
    this.implantChipId = implantChipId;
  }
}
