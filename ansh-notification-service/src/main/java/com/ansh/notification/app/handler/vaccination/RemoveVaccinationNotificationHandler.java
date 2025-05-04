package com.ansh.notification.app.handler.vaccination;

import com.ansh.event.vaccination.RemoveVaccinationEvent;
import com.ansh.notification.NotificationMessages;
import com.ansh.notification.app.handler.AbstractAnimalNotificationHandler;
import org.springframework.stereotype.Component;

@Component
public class RemoveVaccinationNotificationHandler extends AbstractAnimalNotificationHandler {

  @Override
  protected String getNotificationSubject() {
    return NotificationMessages.REMOVE_VACCINE_SUBJECT;
  }

  @Override
  protected String getNotificationTemplate() {
    return NotificationMessages.REMOVE_VACCINE_TEMPLATE;
  }

  @Override
  public Class getHandledEventType() {
    return RemoveVaccinationEvent.class;
  }
}
