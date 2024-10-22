package com.ansh.notification.handler;

import com.ansh.event.AnimalEvent;
import com.ansh.service.EmailService;
import com.ansh.service.TopicSubscriberRegistry;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public abstract class AbstractAnimalNotificationHandler implements AnimalEventNotificationHandler {

  @Autowired
  private EmailService emailService;
  @Autowired
  private TopicSubscriberRegistry topicSubscriberRegistry;

  @Value("${animalShelterNotificationApp}")
  private String animalShelterNotificationApp;

  @Override
  public void handle(AnimalEvent event) {
    sendNotifications(event.getParams(), getNotificationSubject(), getNotificationTemplate());
  }

  protected abstract String getNotificationSubject();

  protected abstract String getNotificationTemplate();

  private void sendNotifications(Map<String, Object> params, String subject, String templateName) {
    topicSubscriberRegistry.getSubscribers("animalGroupId")
        .forEach(email -> {
          params.put("name", email);
          params.put("unsubscribeLink", animalShelterNotificationApp
              + "/unsubscribe/" + email);
          emailService.sendSimpleMessage(email, subject, templateName, params);
        });
  }
}
