package com.ansh.app.service.notification.subscription.impl;

import com.ansh.app.service.notification.subscription.NotificationSubscriptionService;
import com.ansh.dto.NotificationStatusDTO;
import com.ansh.entity.account.UserProfile.AnimalInfoNotifStatus;
import com.ansh.entity.subscription.Subscription;
import com.ansh.notification.external.ExternalNotificationServiceClient;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("notificationSubscriptionService")
public class NotificationSubscriptionServiceImpl implements NotificationSubscriptionService {

  private final static String APPROVER = "approver";
  private final static String EMAIL = "email";
  private final static String TOPIC = "topic";

  private static final Logger LOG = LoggerFactory.getLogger(
      NotificationSubscriptionServiceImpl.class);

  @Autowired
  private ExternalNotificationServiceClient externalNotificationServiceClient;

  private final String registerEndpoint;
  private final String unsubscribeEndpoint;
  private final String allEndpoint;
  private final String statusesEndpoint;

  public NotificationSubscriptionServiceImpl(
      ExternalNotificationServiceClient externalNotificationServiceClient,
      @Value("${notification.subscription.endpoint.register}") String registerEndpoint,
      @Value("${notification.subscription.endpoint.unsubscribe}") String unsubscribeEndpoint,
      @Value("${notification.subscription.endpoint.all}") String allEndpoint,
      @Value("${notification.subscription.endpoint.statuses}") String statusesEndpoint
  ) {
    this.externalNotificationServiceClient = externalNotificationServiceClient;
    this.registerEndpoint = registerEndpoint;
    this.unsubscribeEndpoint = unsubscribeEndpoint;
    this.allEndpoint = allEndpoint;
    this.statusesEndpoint = statusesEndpoint;
  }

  @Override
  public Mono<List<Subscription>> getAllSubscriptionByAccount(String approver) {
    return externalNotificationServiceClient.post(
        allEndpoint,
        Map.of(APPROVER, approver),
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
        Map.of(APPROVER, email),
        new ParameterizedTypeReference<NotificationStatusDTO>() {
        }
    ).onErrorResume(error -> {
      LOG.warn("[approver status] Error {}", error.getMessage());
      return Mono.just(new NotificationStatusDTO(AnimalInfoNotifStatus.UNKNOWN,
          AnimalInfoNotifStatus.UNKNOWN, AnimalInfoNotifStatus.UNKNOWN));
    });
  }

  public Mono<Boolean> registerSubscriber(String email, String approver, String topic) {
    String resolvedApprover = Optional.ofNullable(approver).orElse("");

    return externalNotificationServiceClient.post(
            registerEndpoint,
            Map.of(
                EMAIL, email,
                APPROVER, resolvedApprover,
                TOPIC, topic
            ),
            new ParameterizedTypeReference<Subscription>() {
            }
        ).thenReturn(true)
        .onErrorResume(error -> {
          LOG.warn("[register subscriber] Error: {}", error.getMessage());
          return Mono.just(false);
        });
  }

  @Override
  public Mono<Boolean> unsubscribe(String email, String approver, String topic) {
    return externalNotificationServiceClient.post(
        unsubscribeEndpoint,
        Map.of(
            EMAIL, email,
            APPROVER, approver,
            TOPIC, topic
        ),
        new ParameterizedTypeReference<String>() {
        }
    ).map(subscription -> {
      LOG.debug("[unsubscribe] Unsubscribed: {}", email);
      return true;
    }).onErrorResume(error -> {
      LOG.warn("[unsubscribe] Error: {}", error.getMessage());
      return Mono.just(false);
    });
  }
}
