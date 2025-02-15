package com.ansh.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateVaccinationInput {
  private Long id;
  private String vaccine;
  private String batch;
  private String email;
  private LocalDate vaccinationTime;
  private String comments;
}
