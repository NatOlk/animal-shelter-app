package com.ansh.event.subscription;

import lombok.Data;

@Data
public class AnimalNotificationUserSubscribedEvent {
  private String email;
  private String approver;
  private String topic;
  private boolean reject;
}
