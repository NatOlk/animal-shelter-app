package com.ansh.notification.handler.vaccination;

import com.ansh.event.AddVaccinationEvent;
import com.ansh.notification.handler.AbstractAnimalNotificationHandler;
import com.ansh.notification.handler.SubscriptionMessages;
import org.springframework.stereotype.Component;

@Component
public class AddVaccinationNotificationHandler extends AbstractAnimalNotificationHandler {

  @Override
  protected String getNotificationSubject() {
    return SubscriptionMessages.ADD_VACCINE_SUBJECT;
  }

  @Override
  protected String getNotificationTemplate() {
    return SubscriptionMessages.ADD_VACCINE_TEMPLATE;
  }

  @Override
  public Class getHandledEventType() {
    return AddVaccinationEvent.class;
  }
}
