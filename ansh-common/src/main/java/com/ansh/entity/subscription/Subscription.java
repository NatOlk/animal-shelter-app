package com.ansh.entity.subscription;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "subscribers", schema = "public")
@Data
public class Subscription {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  @Column
  private String email;
  @Column
  private String approver;
  @Column
  private String topic;
  @Column
  private boolean accepted;
  @Column
  private boolean approved;
  @Column
  private String token;

  public Subscription() {
  }
}
