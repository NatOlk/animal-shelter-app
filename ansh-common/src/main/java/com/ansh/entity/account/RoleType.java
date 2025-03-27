package com.ansh.entity.account;

import lombok.Getter;

@Getter
public enum RoleType {

  USER("USER", 1),

  VOLUNTEER("VOLUNTEER", 1),

  EMPLOYEE("EMPLOYEE", 2),

  DOCTOR("DOCTOR", 2),

  ADMIN("ADMIN", 3);

  private final String name;
  private final int level;

  RoleType(String name, int level) {
    this.name = name;
    this.level = level;
  }
}
