package com.ansh.entity.account;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
import lombok.ToString;

@Entity
@Table(name = "roles", schema = "public")
@Data
public class Role {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(unique = true, nullable = false)
  private String name;

  @Column(nullable = false)
  private int level;

  @ManyToMany(mappedBy = "roles")
  @ToString.Exclude
  private Set<UserProfile> users = new HashSet<>();

  public Role() {
  }

  public Role(RoleType roleType) {
    this.name = roleType.getName();
    this.level = roleType.getLevel();
  }
}

