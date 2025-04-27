package com.ansh.notification.strategy;

import com.ansh.app.service.notification.subscription.PendingSubscriptionService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PendingSubscriptionServiceStrategy {

  private final Map<String, PendingSubscriptionService> subscriptionServices;

  @Autowired
  public PendingSubscriptionServiceStrategy(List<PendingSubscriptionService> services) {
    this.subscriptionServices = services.stream()
        .collect(Collectors.toMap(PendingSubscriptionService::getTopicId, service -> service));
  }

  public Optional<PendingSubscriptionService> getServiceByTopic(String topic) {
    return Optional.ofNullable(subscriptionServices.get(topic));
  }

  public List<PendingSubscriptionService> getAllServices() {
    return List.copyOf(subscriptionServices.values());
  }
}
