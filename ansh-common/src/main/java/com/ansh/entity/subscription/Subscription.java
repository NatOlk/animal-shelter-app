package com.ansh.entity.subscription;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "subscribers", schema = "public",
    indexes = {
        @Index(name = "idx_email_topic", columnList = "email, topic"),
        @Index(name = "idx_topic", columnList = "topic")
    })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}
