package com.ansh.notification.app.handler.vaccination;

import com.ansh.event.AddVaccinationEvent;
import com.ansh.notification.NotificationMessages;
import com.ansh.notification.app.handler.AbstractAnimalNotificationHandler;
import com.ansh.service.SubscriberRegistryService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class AddVaccinationNotificationHandler extends AbstractAnimalNotificationHandler {

  @Autowired
  @Qualifier("vaccinationTopicSubscriber")
  private SubscriberRegistryService vaccinationTopicSubscriber;

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

  @Override
  protected List<SubscriberRegistryService> getSubscriberRegistryServices() {
    return List.of(vaccinationTopicSubscriber);
  }
}
