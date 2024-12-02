package com.ansh.service;

import static com.ansh.notification.handler.SubscriptionMessages.*;

import com.ansh.entity.subscription.Subscription;
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
    Map<String, Object> params = createEmailParams(sb.getEmail(), sb.getToken(), true);
    emailService.sendSimpleMessage(sb.getEmail(), SUBSCRIPTION_SUBJECT, ACCEPT_SUBSCRIPTION_TEMPLATE, params);
  }

  public void sendSuccessTokenConfirmationEmail(Subscription sb) {
    Map<String, Object> params = createEmailParams(sb.getEmail(), sb.getToken(), false);
    emailService.sendSimpleMessage(sb.getEmail(), SUCCESS_SUBSCRIPTION_SUBJECT, SUCCESS_SUBSCRIPTION_TEMPLATE, params);
  }

  public void sendRepeatConfirmationEmail(Subscription sb) {
    Map<String, Object> params = createEmailParams(sb.getEmail(), sb.getToken(), false);
    emailService.sendSimpleMessage(sb.getEmail(), SUCCESS_SUBSCRIPTION_SUBJECT, REPEAT_SUBSCRIPTION_TEMPLATE, params);
  }

  private Map<String, Object> createEmailParams(String email, String token, boolean includeSubscriptionLink) {
    Map<String, Object> params = new HashMap<>();
    params.put("name", email);

    String unsubscribeLink = animalShelterNotificationApp + "/external/animal-notify-unsubscribe/" + token;
    params.put("unsubscribeLink", unsubscribeLink);

    if (includeSubscriptionLink) {
      String confirmationLink = animalShelterNotificationApp + "/external/animal-notify-subscribe-check/" + token;
      params.put("confirmationLink", confirmationLink);
      params.put("subscriptionLink", confirmationLink);
    }

    return params;
  }

  protected void setAnimalShelterNotificationApp(String animalShelterNotificationApp) {
    this.animalShelterNotificationApp = animalShelterNotificationApp;
  }
}
