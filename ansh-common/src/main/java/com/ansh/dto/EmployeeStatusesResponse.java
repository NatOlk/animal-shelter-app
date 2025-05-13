package com.ansh.dto;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeStatusesResponse {

  private String email;
  private String approver;
  private Map<String, Boolean> topics;
}
