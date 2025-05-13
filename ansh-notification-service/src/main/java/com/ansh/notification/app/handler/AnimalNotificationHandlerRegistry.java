package com.ansh.notification.app.handler;

import com.ansh.event.AnimalShelterEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AnimalNotificationHandlerRegistry {

  private static final Logger LOG = LoggerFactory.getLogger(AnimalNotificationHandlerRegistry.class);
  private final Map<Class<? extends AnimalShelterEvent>, AnimalEventNotificationHandler> handlerMap
      = new HashMap<>();

  @Autowired
  public AnimalNotificationHandlerRegistry(List<AnimalEventNotificationHandler> handlers) {
    for (AnimalEventNotificationHandler handler : handlers) {
      handlerMap.put(handler.getHandledEventType(), handler);
    }
  }

  public void handleEvent(String topic, AnimalShelterEvent event) {
    AnimalEventNotificationHandler handler = handlerMap.get(event.getClass());
    if (handler != null) {
      handler.handle(topic, event);
    } else {
      LOG.error("Can't find a handler for event");
    }
  }
}
