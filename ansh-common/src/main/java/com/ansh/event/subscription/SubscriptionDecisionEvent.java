package com.ansh.event.subscription;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionDecisionEvent {
  private String email;
  private String approver;
  private String topic;
  private boolean reject;
  private LocalDate created;
}
