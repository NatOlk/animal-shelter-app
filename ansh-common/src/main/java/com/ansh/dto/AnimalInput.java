package com.ansh.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnimalInput {
  private String name;
  private String species;
  private String primaryColor;
  private String breed;
  private String implantChipId;
  private String gender;
  private LocalDate birthDate;
  private String pattern;
}
