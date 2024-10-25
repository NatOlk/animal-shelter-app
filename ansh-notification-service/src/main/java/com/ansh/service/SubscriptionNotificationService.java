package com.ansh.service;

import static com.ansh.notification.handler.SubscriptionMessages.ACCEPT_SUBSCRIPTION_TEMPLATE;
import static com.ansh.notification.handler.SubscriptionMessages.REPEAT_SUBSCRIPTION_TEMPLATE;
import static com.ansh.notification.handler.SubscriptionMessages.SUBSCRIPTION_SUBJECT;
import static com.ansh.notification.handler.SubscriptionMessages.SUCCESS_SUBSCRIPTION_SUBJECT;
import static com.ansh.notification.handler.SubscriptionMessages.SUCCESS_SUBSCRIPTION_TEMPLATE;

import com.ansh.repository.entity.Subscription;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionNotificationService {

  @Value("${animalShelterNotificationApp}")
  private String animalShelterNotificationApp;

  @Autowired
  private EmailService emailService;

  public void sendNeedAcceptSubscriptionEmail(Subscription sb) {
    String email = sb.getEmail();
    String token = sb.getToken();
    Map<String, Object> params = new HashMap<>(2);
    params.put("name", email);
    String confirmationLink = animalShelterNotificationApp +
        "/animal-notify-subscribe-check/" + token;
    params.put("confirmationLink", confirmationLink);
    params.put("subscriptionLink", confirmationLink);
    emailService.sendSimpleMessage(email, SUBSCRIPTION_SUBJECT,
        ACCEPT_SUBSCRIPTION_TEMPLATE, params);
  }

  public void sendSuccessTokenConfirmationEmail(Subscription sb) {
    String email = sb.getEmail();
    String token = sb.getToken();
    Map<String, Object> params = new HashMap<>(2);
    params.put("name", email);
    String confirmationLink = animalShelterNotificationApp +
        "/animal-notify-unsubscribe/" + token;
    params.put("unsubscribeLink", confirmationLink);
    emailService.sendSimpleMessage(email, SUCCESS_SUBSCRIPTION_SUBJECT,
        SUCCESS_SUBSCRIPTION_TEMPLATE, params);
  }

  public void sendRepeatConfirmationEmail(Subscription sb) {
    String email = sb.getEmail();
    String token = sb.getToken();
    Map<String, Object> params = new HashMap<>(2);
    params.put("name", email);
    String confirmationLink = animalShelterNotificationApp +
        "/animal-notify-unsubscribe/" + token;
    params.put("unsubscribeLink", confirmationLink);
    emailService.sendSimpleMessage(email, SUCCESS_SUBSCRIPTION_SUBJECT,
        REPEAT_SUBSCRIPTION_TEMPLATE, params);
  }
}
