package com.ansh.notification.app.handler.vaccination;

import com.ansh.event.AddVaccinationEvent;
import com.ansh.notification.NotificationMessages;
import com.ansh.notification.app.handler.AbstractAnimalNotificationHandler;
import org.springframework.stereotype.Component;

@Component
public class AddVaccinationNotificationHandler extends AbstractAnimalNotificationHandler {

  @Override
  protected String getNotificationSubject() {
    return NotificationMessages.ADD_VACCINE_SUBJECT;
  }

  @Override
  protected String getNotificationTemplate() {
    return NotificationMessages.ADD_VACCINE_TEMPLATE;
  }

  @Override
  public Class getHandledEventType() {
    return AddVaccinationEvent.class;
  }
}
