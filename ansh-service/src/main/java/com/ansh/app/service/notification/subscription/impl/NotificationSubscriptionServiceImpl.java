package com.ansh.app.service.notification.subscription.impl;

import com.ansh.app.service.notification.subscription.NotificationSubscriptionService;
import com.ansh.dto.NotificationStatusDTO;
import com.ansh.entity.account.UserProfile.AnimalInfoNotifStatus;
import com.ansh.entity.subscription.Subscription;
import com.ansh.notification.external.ExternalNotificationServiceClient;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("notificationSubscriptionService")
public class NotificationSubscriptionServiceImpl implements NotificationSubscriptionService {

  private static final Logger LOG = LoggerFactory.getLogger(
      NotificationSubscriptionServiceImpl.class);

  @Autowired
  private ExternalNotificationServiceClient externalNotificationServiceClient;

  @Value("${notification.subscription.endpoint.register}")
  private String registerEndpoint;

  @Value("${notification.subscription.endpoint.unsubscribe}")
  private String unsubscribeEndpoint;

  @Value("${notification.subscription.endpoint.all}")
  private String allEndpoint;

  @Value("${notification.subscription.endpoint.statuses}")
  private String statusesEndpoint;

  @Override
  public Mono<List<Subscription>> getAllSubscriptionByAccount(String approver) {
    return externalNotificationServiceClient.post(
        allEndpoint,
        Map.of("approver", approver),
        new ParameterizedTypeReference<List<Subscription>>() {
        }
    ).onErrorResume(error -> {
      LOG.warn("[approver subscriptions] Error {}", error.getMessage());
      return Mono.just(Collections.emptyList());
    });
  }

  @Override
  public Mono<NotificationStatusDTO> getStatusesByAccount(String email) {
    return externalNotificationServiceClient.post(
        statusesEndpoint,
        Map.of("approver", email),
        new ParameterizedTypeReference<NotificationStatusDTO>() {
        }
    ).onErrorResume(error -> {
      LOG.warn("[approver status] Error {}", error.getMessage());
      return Mono.just(new NotificationStatusDTO(AnimalInfoNotifStatus.UNKNOWN,
          AnimalInfoNotifStatus.UNKNOWN, AnimalInfoNotifStatus.UNKNOWN));
    });
  }

  @Override
  public void registerSubscriber(String email, String approver, String topic) {
    externalNotificationServiceClient.post(
        registerEndpoint,
        Map.of(
            "email", email,
            "approver", approver,
            "topic", topic
        ),
        new ParameterizedTypeReference<Subscription>() {
        }
    ).onErrorResume(error -> {
      LOG.warn("[register subscriber] Error: {}", error.getMessage());
      return Mono.empty();
    }).subscribe();
  }

  @Override
  public void unsubscribe(String email, String approver, String topic) {
    externalNotificationServiceClient.post(
        unsubscribeEndpoint,
        Map.of(
            "email", email,
            "approver", approver,
            "topic", topic
        ),
        new ParameterizedTypeReference<Subscription>() {
        }
    ).onErrorResume(error -> {
      LOG.warn("[unregister subscriber] Error: {}", error.getMessage());
      return Mono.empty();
    }).subscribe();
  }

  protected void setAllEndpoint(String allEndpoint) {
    this.allEndpoint = allEndpoint;
  }

  protected void setStatusesEndpoint(String statusesEndpoint) {
    this.statusesEndpoint = statusesEndpoint;
  }
}
