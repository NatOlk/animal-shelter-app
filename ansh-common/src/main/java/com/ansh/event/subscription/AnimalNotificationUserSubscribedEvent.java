package com.ansh.event.subscription;

import lombok.Data;

@Data
public class AnimalNotificationUserSubscribedEvent {
  private String email;
}