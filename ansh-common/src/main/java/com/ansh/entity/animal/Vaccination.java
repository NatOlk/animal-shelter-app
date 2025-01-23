package com.ansh.entity.animal;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "vaccinations", schema = "public",
    uniqueConstraints = {
        @UniqueConstraint(name = "unique_vaccination_attributes",
            columnNames = {"vaccine", "batch"})
    })
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Vaccination {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @EqualsAndHashCode.Include
  private Long id;

  @Column
  private String vaccine;

  @Column(unique = true)
  private String batch;

  @Column
  private String email;

  @Column
  private LocalDate vaccinationTime;

  @Column
  private String comments;

  @JsonBackReference
  @ToString.Exclude
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "animal_id")
  private Animal animal;
}
