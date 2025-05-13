package com.ansh.notification.app.handler.news;

import com.ansh.event.news.AnimalShelterNewsEvent;
import com.ansh.notification.NotificationMessages;
import com.ansh.notification.app.handler.AbstractAnimalNotificationHandler;

public class AnimalShelterNewsNotificationHandler extends AbstractAnimalNotificationHandler {

  @Override
  protected String getNotificationSubject() {
    return NotificationMessages.ANIMAL_NEWS_SUBJECT;
  }

  @Override
  protected String getNotificationTemplate() {
    return NotificationMessages.ANIMAL_NEWS_TEMPLATE;
  }

  @Override
  public Class<? extends AnimalShelterNewsEvent> getHandledEventType() {
    return AnimalShelterNewsEvent.class;
  }
}
