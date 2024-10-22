package com.ansh.notification.handler;

import com.ansh.event.RemoveVaccinationEvent;
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

  /**
   * @return
   */
  @Override
  public Class getHandledEventType() {
    return RemoveVaccinationEvent.class;
  }
}
