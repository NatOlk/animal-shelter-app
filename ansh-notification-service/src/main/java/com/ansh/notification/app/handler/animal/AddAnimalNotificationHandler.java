package com.ansh.notification.app.handler.animal;

import com.ansh.event.animal.AddAnimalEvent;
import com.ansh.notification.NotificationMessages;
import com.ansh.notification.app.handler.AbstractAnimalNotificationHandler;
import org.springframework.stereotype.Component;

@Component
public class AddAnimalNotificationHandler extends AbstractAnimalNotificationHandler {

  @Override
  protected String getNotificationSubject() {
    return NotificationMessages.ADD_ANIMAL_SUBJECT;
  }

  @Override
  protected String getNotificationTemplate() {
    return NotificationMessages.ADD_ANIMAL_TEMPLATE;
  }

  @Override
  public Class getHandledEventType() {
    return AddAnimalEvent.class;
  }
}
