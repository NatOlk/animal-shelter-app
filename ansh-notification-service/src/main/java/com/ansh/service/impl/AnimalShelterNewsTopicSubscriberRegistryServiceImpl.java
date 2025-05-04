package com.ansh.service.impl;

import com.ansh.entity.subscription.Subscription;
import com.ansh.event.AnimalShelterTopic;
import com.ansh.service.SubscriberRegistryService;
import com.ansh.service.SubscriptionNotificationEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("animalShelterNewsSubscriber")
public class AnimalShelterNewsTopicSubscriberRegistryServiceImpl extends
    AbstractSubscriberRegistryService implements SubscriberRegistryService {

  @Autowired
  protected SubscriptionNotificationEmailService subscriptionNotificationEmailService;

  @Override
  public String getTopicId() {
    return AnimalShelterTopic.ANIMAL_SHELTER_NEWS.getTopicName();
  }

  @Override
  protected void handleExistingSubscription(Subscription subscription) {
    if (!subscription.isApproved()) {
      return;
    }
    if (subscription.isAccepted()) {
      subscriptionNotificationEmailService.sendRepeatConfirmationEmail(subscription);
    } else {
      subscriptionNotificationEmailService.sendNeedAcceptSubscriptionEmail(subscription);
    }
  }

  @Override
  protected void sendSuccessTokenConfirmationEmail(Subscription subscription) {
    subscriptionNotificationEmailService.sendSuccessTokenConfirmationEmail(subscription);
  }

  @Override
  protected void sendNeedAcceptSubscriptionEmail(Subscription subscription) {
    subscriptionNotificationEmailService.sendNeedAcceptSubscriptionEmail(subscription);
  }
}
