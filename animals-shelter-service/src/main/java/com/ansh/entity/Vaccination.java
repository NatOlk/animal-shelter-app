package com.ansh.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Date;
import lombok.Data;

@Entity
@Table(name = "vaccinations", schema = "public")
@Data
public class Vaccination {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  @Column
  private String vaccine;
  @Column(unique = true)
  private String batch;
  @Column
  private String email;
  @Column
  private Date vaccinationTime;
  @Column
  private String comments;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "animal_id")
  private Animal animal;
}
