package com.ansh.app.service.notification.subscription.impl;

import com.ansh.app.service.notification.subscription.NotificationSubscriptionService;
import com.ansh.entity.animal.UserProfile.AnimalInfoNotifStatus;
import com.ansh.entity.subscription.Subscription;
import com.ansh.notification.external.ExternalNotificationServiceClient;
import com.ansh.utils.IdentifierMasker;
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

  private static final Logger LOG = LoggerFactory.getLogger(NotificationSubscriptionServiceImpl.class);

  @Autowired
  private ExternalNotificationServiceClient externalNotificationServiceClient;

  @Value("${notification.subscription.endpoint.allByApprover}")
  private String allByApproverEndpoint;

  @Value("${notification.subscription.endpoint.statusByApprover}")
  private String statusByApproverEndpoint;

  @Override
  public Mono<List<Subscription>> getAllSubscriptionByApprover(String approver) {
    return externalNotificationServiceClient.post(
        allByApproverEndpoint,
        Map.of("approver", approver),
        new ParameterizedTypeReference<List<Subscription>>() {}
    ).onErrorResume(error -> {
      LOG.warn("[approver subscriptions] Error {}", error.getMessage());
      return Mono.just(Collections.emptyList());
    });
  }

  @Override
  public Mono<AnimalInfoNotifStatus> getAnimalInfoStatusByApprover(String approver) {
    return externalNotificationServiceClient.post(
        statusByApproverEndpoint,
        Map.of("approver", approver),
        new ParameterizedTypeReference<AnimalInfoNotifStatus>() {}
    ).onErrorResume(error -> {
      LOG.warn("[approver status] Error {}", error.getMessage());
      return Mono.just(AnimalInfoNotifStatus.UNKNOWN);
    });
  }

  protected void setAllByApproverEndpoint(String allByApproverEndpoint) {
    this.allByApproverEndpoint = allByApproverEndpoint;
  }

  protected void setStatusByApproverEndpoint(String statusByApproverEndpoint) {
    this.statusByApproverEndpoint = statusByApproverEndpoint;
  }
}
