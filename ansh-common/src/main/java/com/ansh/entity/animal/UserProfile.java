package com.ansh.entity.animal;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "user_profiles", schema = "public")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserProfile {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  private Long id;

  @Column(unique = true)
  @EqualsAndHashCode.Include
  private String email;

  @Column(unique = true)
  private String name;

  @Column
  private String password;

  @Enumerated(EnumType.STRING)
  @Column(name = "animal_notif_status", nullable = false)
  private AnimalNotifStatus animalNotifyStatus = AnimalNotifStatus.NONE;

  @ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
  @JoinTable(
      name = "user_roles",
      joinColumns = {@JoinColumn(name = "user_id")},
      inverseJoinColumns = {@JoinColumn(name = "role_id")}
  )
  private Set<Role> roles = new HashSet<>();

  public void addRole(Role role) {
    this.roles.add(role);
    role.getUsers().add(this);
  }

  public enum AnimalNotifStatus {
    NONE,
    PENDING,
    ACTIVE
  }
}

