package com.ansh.notification.app.handler.animal;

import com.ansh.event.AddAnimalEvent;
import com.ansh.notification.NotificationMessages;
import com.ansh.notification.app.handler.AbstractAnimalNotificationHandler;
import com.ansh.service.SubscriberRegistryService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class AddAnimalNotificationHandler extends AbstractAnimalNotificationHandler {

  @Autowired
  @Qualifier("animalTopicSubscriber")
  private SubscriberRegistryService animalTopicSubscriber;

  @Autowired
  @Qualifier("animalShelterNewsSubscriber")
  private SubscriberRegistryService animalShelterNewsSubscriber;

  @Override
  protected String getNotificationSubject() {
    return NotificationMessages.ADD_ANIMAL_SUBJECT;
  }

  @Override
  protected String getNotificationTemplate() {
    return NotificationMessages.ADD_ANIMAL_TEMPLATE;
  }

  @Override
  public Class getHandledEventType() {
    return AddAnimalEvent.class;
  }

  @Override
  protected List<SubscriberRegistryService> getSubscriberRegistryServices() {
    return List.of(animalShelterNewsSubscriber, animalTopicSubscriber);
  }
}
