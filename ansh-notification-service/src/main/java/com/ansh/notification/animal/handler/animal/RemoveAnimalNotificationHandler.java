package com.ansh.notification.animal.handler.animal;

import com.ansh.event.RemoveAnimalEvent;
import com.ansh.notification.animal.handler.AbstractAnimalNotificationHandler;
import com.ansh.notification.NotificationMessages;
import org.springframework.stereotype.Component;

@Component
public class RemoveAnimalNotificationHandler extends AbstractAnimalNotificationHandler {

  @Override
  protected String getNotificationSubject() {
    return NotificationMessages.REMOVE_ANIMAL_SUBJECT;
  }

  @Override
  protected String getNotificationTemplate() {
    return NotificationMessages.REMOVE_ANIMAL_TEMPLATE;
  }

  @Override
  public Class getHandledEventType() {
    return RemoveAnimalEvent.class;
  }
}
