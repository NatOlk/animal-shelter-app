package com.ansh.notification.handler.vaccination;

import com.ansh.event.RemoveVaccinationEvent;
import com.ansh.notification.handler.AbstractAnimalNotificationHandler;
import com.ansh.notification.handler.SubscriptionMessages;
import org.springframework.stereotype.Component;

@Component
public class RemoveVaccinationNotificationHandler extends AbstractAnimalNotificationHandler {

  @Override
  protected String getNotificationSubject() {
    return SubscriptionMessages.REMOVE_VACCINE_SUBJECT;
  }

  @Override
  protected String getNotificationTemplate() {
    return SubscriptionMessages.REMOVE_VACCINE_TEMPLATE;
  }
  @Override
  public Class getHandledEventType() {
    return RemoveVaccinationEvent.class;
  }
}
