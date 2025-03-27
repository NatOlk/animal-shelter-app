package com.ansh.entity.account;

import jakarta.persistence.*;
import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user_profiles", schema = "public")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserProfile {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @EqualsAndHashCode.Include
  private Long id;

  @Column(unique = true, nullable = false)
  @EqualsAndHashCode.Include
  private String email;

  @Column(unique = true, nullable = false)
  private String name;

  @Column(nullable = false)
  private String password;

  @Enumerated(EnumType.STRING)
  @Column(name = "animal_notif_status", nullable = false)
  private AnimalInfoNotifStatus animalNotifyStatus = AnimalInfoNotifStatus.NONE;

  @Transient
  private Set<Role> roles = new HashSet<>();

  @Column(name = "roles")
  private String rolesRaw;

  public Set<Role> getRoles() {
    if (rolesRaw == null || rolesRaw.isBlank()) return Set.of();
    return Arrays.stream(rolesRaw.split(","))
        .map(String::trim)
        .map(Role::valueOf)
        .collect(Collectors.toSet());
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
    this.rolesRaw = roles.stream()
        .map(Enum::name)
        .collect(Collectors.joining(","));
  }

  public enum AnimalInfoNotifStatus {
    UNKNOWN,
    NONE,
    PENDING,
    ACTIVE
  }

  public enum Role {

    USER,

    VOLUNTEER,

    EMPLOYEE,

    DOCTOR,

    ADMIN
  }
}
