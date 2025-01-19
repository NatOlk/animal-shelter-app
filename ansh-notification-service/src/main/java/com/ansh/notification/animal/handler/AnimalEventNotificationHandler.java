package com.ansh.notification.animal.handler;

import com.ansh.event.AnimalEvent;

public interface AnimalEventNotificationHandler {

  Class<? extends AnimalEvent> getHandledEventType();
  void handle(AnimalEvent event);
}
