package com.ansh.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pending_subscribers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PendingSubscriber {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false)
  private String email;
  @Column
  private String approver;
  @Column
  private String topic;
  @Column
  private Boolean approved = false;

  public PendingSubscriber(String email, String approver, String topic) {
    this.email = email;
    this.approver = approver;
    this.topic = topic;
  }
}

