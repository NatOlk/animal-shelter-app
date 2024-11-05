package com.ansh.notification.handler.animal;

import com.ansh.event.RemoveAnimalEvent;
import com.ansh.notification.handler.AbstractAnimalNotificationHandler;
import com.ansh.notification.handler.SubscriptionMessages;
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
