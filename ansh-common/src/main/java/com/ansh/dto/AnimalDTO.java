package com.ansh.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnimalDTO {

  private Long id;
  private String name;
  private String species;
  private String primaryColor;
  private String implantChipId;
  private String breed;
  private char gender;
  private LocalDate birthDate;
  private String pattern;
  private LocalDate admissionDate;
  private String photoImgPath;
}
