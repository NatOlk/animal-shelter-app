package com.ansh.notification.handler;

import com.ansh.event.AnimalEvent;
import com.ansh.service.EmailService;
import com.ansh.service.TopicSubscriberRegistryService;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public abstract class AbstractAnimalNotificationHandler implements AnimalEventNotificationHandler {

  private static final Logger LOG = LoggerFactory.getLogger(
      AbstractAnimalNotificationHandler.class);
  @Value("${animalShelterNotificationApp}")
  private String animalShelterNotificationApp;
  @Autowired
  private EmailService emailService;
  @Autowired
  private TopicSubscriberRegistryService topicSubscriberRegistry;

  @Override
  public void handle(AnimalEvent event) {
    sendNotifications(event.getParams(), getNotificationSubject(), getNotificationTemplate());
  }

  protected abstract String getNotificationSubject();

  protected abstract String getNotificationTemplate();

  private void sendNotifications(Map<String, Object> params, String subject, String templateName) {
    topicSubscriberRegistry.getSubscribers()
        .forEach(subscription -> {
          params.put("name", subscription.getEmail());
          params.put("unsubscribeLink", animalShelterNotificationApp
              + "/external/animal-notify-unsubscribe/" + subscription.getToken());
          emailService.sendSimpleMessage(subscription.getEmail(), subject, templateName, params);
        });
  }
}
