package com.ansh.event.subscription;

import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionDecisionEvent {
  private String eventId = UUID.randomUUID().toString();
  private String email;
  private String approver;
  private String topic;
  private boolean reject;
  private LocalDate created;
}
