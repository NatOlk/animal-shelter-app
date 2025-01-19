package com.ansh.service;

import static com.ansh.notification.NotificationMessages.ACCEPT_SUBSCRIPTION_TEMPLATE;
import static com.ansh.notification.NotificationMessages.REPEAT_SUBSCRIPTION_TEMPLATE;
import static com.ansh.notification.NotificationMessages.SUBSCRIPTION_SUBJECT;
import static com.ansh.notification.NotificationMessages.SUCCESS_SUBSCRIPTION_SUBJECT;
import static com.ansh.notification.NotificationMessages.SUCCESS_SUBSCRIPTION_TEMPLATE;

import com.ansh.entity.subscription.Subscription;
import com.ansh.utils.EmailParamsBuilder;
import com.ansh.utils.LinkGenerator;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionNotificationEmailService {

  @Autowired
  private EmailService emailService;
  @Autowired
  private LinkGenerator linkGenerator;

  public void sendNeedAcceptSubscriptionEmail(Subscription sb) {
    Map<String, Object> params = createEmailParams(sb.getEmail(), sb.getToken(), true);
    emailService.sendSimpleMessage(sb.getEmail(), SUBSCRIPTION_SUBJECT,
        ACCEPT_SUBSCRIPTION_TEMPLATE, params);
  }

  public void sendSuccessTokenConfirmationEmail(Subscription sb) {
    Map<String, Object> params = createEmailParams(sb.getEmail(), sb.getToken(), false);
    emailService.sendSimpleMessage(sb.getEmail(), SUCCESS_SUBSCRIPTION_SUBJECT,
        SUCCESS_SUBSCRIPTION_TEMPLATE, params);
  }

  public void sendRepeatConfirmationEmail(Subscription sb) {
    Map<String, Object> params = createEmailParams(sb.getEmail(), sb.getToken(), false);
    emailService.sendSimpleMessage(sb.getEmail(), SUCCESS_SUBSCRIPTION_SUBJECT,
        REPEAT_SUBSCRIPTION_TEMPLATE, params);
  }

  public Map<String, Object> createEmailParams(String email, String token, boolean includeSubscriptionLink) {
    EmailParamsBuilder builder = new EmailParamsBuilder()
        .name(email)
        .unsubscribeLink(linkGenerator.generateUnsubscribeLink(token));

    if (includeSubscriptionLink) {
      String confirmationLink = linkGenerator.generateConfirmationLink(token);
      builder.confirmationLink(confirmationLink);
      builder.subscriptionLink(confirmationLink);
    }

    return builder.build();
  }
}
