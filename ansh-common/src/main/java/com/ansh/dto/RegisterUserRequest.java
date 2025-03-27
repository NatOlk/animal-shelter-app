package com.ansh.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserRequest {
  private String identifier;
  private String email;
  private String password;
}
