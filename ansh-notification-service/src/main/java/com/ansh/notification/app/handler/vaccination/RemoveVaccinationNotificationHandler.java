package com.ansh.notification.app.handler.vaccination;

import com.ansh.event.RemoveVaccinationEvent;
import com.ansh.notification.NotificationMessages;
import com.ansh.notification.app.handler.AbstractAnimalNotificationHandler;
import com.ansh.service.SubscriberRegistryService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class RemoveVaccinationNotificationHandler extends AbstractAnimalNotificationHandler {

  @Autowired
  @Qualifier("vaccinationTopicSubscriber")
  private SubscriberRegistryService vaccinationTopicSubscriber;

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

  @Override
  protected List<SubscriberRegistryService> getSubscriberRegistryServices() {
    return List.of(vaccinationTopicSubscriber);
  }
}
