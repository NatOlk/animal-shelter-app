package com.ansh.notification.strategy;

import com.ansh.notification.app.AnimalShelterNotificationProducer;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotificationProducerStrategy {

  private final Map<String, AnimalShelterNotificationProducer> notificationProducerMap;

  @Autowired
  public NotificationProducerStrategy(List<AnimalShelterNotificationProducer> services) {
    this.notificationProducerMap = services.stream()
        .collect(
            Collectors.toMap(AnimalShelterNotificationProducer::getTopicId, service -> service));
  }

  public Optional<AnimalShelterNotificationProducer> getServiceByTopic(String topic) {
    return Optional.ofNullable(notificationProducerMap.get(topic));
  }

  public List<AnimalShelterNotificationProducer> getAllServices() {
    return List.copyOf(notificationProducerMap.values());
  }
}
