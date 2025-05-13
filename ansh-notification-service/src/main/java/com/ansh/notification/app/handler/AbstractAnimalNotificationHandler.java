package com.ansh.notification.app.handler;

import com.ansh.entity.subscription.Subscription;
import com.ansh.event.AnimalShelterEvent;
import com.ansh.service.EmailService;
import com.ansh.service.SubscriberRegistryService;
import com.ansh.strategy.SubscriberRegistryServiceStrategy;
import com.ansh.utils.EmailParamsBuilder;
import com.ansh.utils.IdentifierMasker;
import com.ansh.utils.LinkGenerator;
import java.util.Collections;
import java.util.List;
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
  private Executor emailNotificationExecutor;

  @Autowired
  private LinkGenerator linkGenerator;

  @Autowired
  private SubscriberRegistryServiceStrategy subscriberRegistryServiceStrategy;

  @Override
  public void handle(String topic, AnimalShelterEvent event) {
    if (!getHandledEventType().isInstance(event)) {
      LOG.error("Incorrect event type: expected {}, but got {}",
          getHandledEventType().getSimpleName(), event.getClass().getSimpleName());
      throw new IllegalArgumentException(
          STR."Invalid event type for handler: \{event.getClass().getSimpleName()}");
    }
    sendNotifications(topic, event.getParams(), getNotificationSubject(),
        getNotificationTemplate());
  }

  protected abstract String getNotificationSubject();

  protected abstract String getNotificationTemplate();

  private void sendNotifications(String topic, Map<String, Object> params, String subj,
      String template) {
    getAcceptedAndApprovedSubscribers(topic)
        .forEach(subscription -> {
          Map<String, Object> personalParams = new EmailParamsBuilder()
              .name(subscription.getEmail())
              .unsubscribeLink(linkGenerator.generateUnsubscribeLink(subscription.getToken()))
              .build();

          personalParams.putAll(params);

          emailNotificationExecutor.execute(() -> {
            LOG.debug("Thread: {} is sending email to {}",
                Thread.currentThread().getName(),
                IdentifierMasker.maskEmail(subscription.getEmail()));
            emailService.sendEmail(subscription.getEmail(), subj, template, personalParams);
          });
        });
  }

  private List<Subscription> getAcceptedAndApprovedSubscribers(String topic) {
    return subscriberRegistryServiceStrategy.getServiceByTopic(topic)
        .map(SubscriberRegistryService::getAcceptedAndApprovedSubscribers)
        .orElse(Collections.emptyList());
  }

}