package com.ansh.notification.app;

import com.ansh.event.AnimalShelterEvent;

public interface AnimalShelterNotificationProducer {

  /**
   * Returns the unique identifier of the topic associated with this subscription service.
   * <p>
   * This identifier is used to determine which Kafka topic the subscription service is responsible
   * for handling. It allows dynamic selection of the appropriate service based on the topic name
   * when processing subscription-related events.
   * </p>
   *
   * @return the topic ID associated with this service
   */
  String getTopicId();

  //TODO add specs
  void sendNotification(AnimalShelterEvent event);
}
