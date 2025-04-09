package com.ansh.event.subscription;

import java.time.LocalDate;
import lombok.Data;

@Data
public class SubscriptionDecisionEvent {
  private String email;
  private String approver;
  private String topic;
  private boolean reject;
  private LocalDate created;
}
