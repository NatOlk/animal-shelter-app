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

  /**
   * Sends a notification based on the provided {@link AnimalShelterEvent}.
   * <p>
   * Implementations should handle serialization, formatting, and dispatching of the event to
   * subscribers (e.g., via email or other channels).
   * </p>
   *
   * @param event the event containing animal shelter-related data to be used in the notification
   */
  void sendNotification(AnimalShelterEvent event);
}
