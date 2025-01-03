package com.ansh.notification.handler.animal;

import com.ansh.event.AddAnimalEvent;
import com.ansh.notification.handler.AbstractAnimalNotificationHandler;
import com.ansh.notification.handler.SubscriptionMessages;
import org.springframework.stereotype.Component;

@Component
public class AddAnimalNotificationHandler extends AbstractAnimalNotificationHandler {

  @Override
  protected String getNotificationSubject() {
    return SubscriptionMessages.ADD_ANIMAL_SUBJECT;
  }

  @Override
  protected String getNotificationTemplate() {
    return SubscriptionMessages.ADD_ANIMAL_TEMPLATE;
  }

  @Override
  public Class getHandledEventType() {
    return AddAnimalEvent.class;
  }
}
