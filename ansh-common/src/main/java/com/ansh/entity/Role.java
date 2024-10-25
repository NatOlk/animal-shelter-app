package com.ansh.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;

@Entity
@Table(name = "roles", schema = "public")
@Data
public class Role {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column
  private String name;

  @ManyToMany(mappedBy = "roles")
  private Set<UserProfile> users = new HashSet<>();

  public Role() {
  }

  public Role(String name) {
    this.name = name;
  }
}

