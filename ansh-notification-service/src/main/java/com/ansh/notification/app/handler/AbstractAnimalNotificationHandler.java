package com.ansh.notification.app.handler;

import com.ansh.event.AnimalEvent;
import com.ansh.service.AnimalTopicSubscriberRegistryService;
import com.ansh.service.EmailService;
import com.ansh.utils.EmailParamsBuilder;
import com.ansh.utils.IdentifierMasker;
import com.ansh.utils.LinkGenerator;
import java.util.Map;
import java.util.concurrent.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractAnimalNotificationHandler implements AnimalEventNotificationHandler {

  private static final Logger LOG = LoggerFactory.getLogger(
      AbstractAnimalNotificationHandler.class);
  @Autowired
  private EmailService emailService;
  @Autowired
  private AnimalTopicSubscriberRegistryService animalTopicSubscriberRegistryService;
  @Autowired
  private Executor emailNotificationExecutor;
  @Autowired
  private LinkGenerator linkGenerator;

  @Override
  public void handle(AnimalEvent event) {
    if (!getHandledEventType().isInstance(event)) {
      LOG.error("Incorrect event type: expected {}, but got {}", getHandledEventType().getSimpleName(), event.getClass().getSimpleName());
      throw new IllegalArgumentException(
          STR."Invalid event type for handler: \{event.getClass().getSimpleName()}");
    }
    sendNotifications(event.getParams(), getNotificationSubject(), getNotificationTemplate());
  }

  protected abstract String getNotificationSubject();

  protected abstract String getNotificationTemplate();

  private void sendNotifications(Map<String, Object> params, String subject, String templateName) {
    animalTopicSubscriberRegistryService.getAcceptedAndApprovedSubscribers()
        .forEach(subscription -> {
          params.putAll(new EmailParamsBuilder()
              .name(subscription.getEmail())
              .unsubscribeLink(
                  linkGenerator.generateUnsubscribeLink(subscription.getToken()))
              .build());

          emailNotificationExecutor.execute(() -> {
                LOG.debug("Thread: {} is sending email to {}", Thread.currentThread().getName(),
                    IdentifierMasker.maskEmail(subscription.getEmail()));
                emailService.sendEmail(subscription.getEmail(), subject, templateName, params);
              }
          );
        });
  }
}
