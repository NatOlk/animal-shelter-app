package com.ansh.strategy;

import com.ansh.service.SubscriberRegistryService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SubscriberRegistryServiceStrategy {

  private final Map<String, SubscriberRegistryService> subscriberRegistryServices;

  @Autowired
  public SubscriberRegistryServiceStrategy(List<SubscriberRegistryService> services) {
    this.subscriberRegistryServices = services.stream()
        .collect(Collectors.toMap(SubscriberRegistryService::getTopicId, service -> service));
  }

  public Optional<SubscriberRegistryService> getServiceByTopic(String topic) {
    return Optional.ofNullable(subscriberRegistryServices.get(topic));
  }

  public List<SubscriberRegistryService> getAllServices() {
    return List.copyOf(subscriberRegistryServices.values());
  }
}
