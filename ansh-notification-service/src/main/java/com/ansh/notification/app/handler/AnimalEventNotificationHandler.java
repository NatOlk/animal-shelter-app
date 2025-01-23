package com.ansh.notification.app.handler;

import com.ansh.event.AnimalEvent;

public interface AnimalEventNotificationHandler {

  Class<? extends AnimalEvent> getHandledEventType();
  void handle(AnimalEvent event);
}
