package com.ansh.notification.handler;

import com.ansh.event.RemoveAnimalEvent;
import org.springframework.stereotype.Component;

@Component
public class RemoveAnimalNotificationHandler extends AbstractAnimalNotificationHandler {

  @Override
  protected String getNotificationSubject() {
    return SubscriptionMessages.REMOVE_ANIMAL_SUBJECT;
  }

  @Override
  protected String getNotificationTemplate() {
    return SubscriptionMessages.REMOVE_ANIMAL_TEMPLATE;
  }

  @Override
  public Class getHandledEventType() {
    return RemoveAnimalEvent.class;
  }
}
