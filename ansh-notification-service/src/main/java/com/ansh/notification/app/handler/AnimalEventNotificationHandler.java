package com.ansh.notification.app.handler;

import com.ansh.event.AnimalShelterEvent;

public interface AnimalEventNotificationHandler {

  Class<? extends AnimalShelterEvent> getHandledEventType();
  void handle(String topic, AnimalShelterEvent event);
}