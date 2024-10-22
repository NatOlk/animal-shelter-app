package com.ansh.notification.handler;

import com.ansh.event.AddVaccinationEvent;
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
