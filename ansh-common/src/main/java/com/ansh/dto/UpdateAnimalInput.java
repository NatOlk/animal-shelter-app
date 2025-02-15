package com.ansh.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAnimalInput {
  private Long id;
  private String primaryColor;
  private String breed;
  private String gender;
  private LocalDate birthDate;
  private String pattern;
  private String photoImgPath;
}
