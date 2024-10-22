package com.ansh.notification.handler;

import com.ansh.event.AnimalEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AnimalEventHandlerRegistry {

  private static final Logger LOG = LoggerFactory.getLogger(AnimalEventHandlerRegistry.class);
  private final Map<Class<? extends AnimalEvent>, AnimalEventNotificationHandler> handlerMap
      = new HashMap<>();

  @Autowired
  public AnimalEventHandlerRegistry(List<AnimalEventNotificationHandler> handlers) {
    for (AnimalEventNotificationHandler handler : handlers) {
      handlerMap.put(handler.getHandledEventType(), handler);
    }
  }

  public void handleEvent(AnimalEvent event) {
    AnimalEventNotificationHandler handler = handlerMap.get(event.getClass());
    if (handler != null) {
      handler.handle(event);
    } else {
      LOG.error("Can't find a handler for event");
    }
  }

}
