package com.ansh.notification.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.ansh.notification.app.AnimalShelterNotificationProducer;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NotificationProducerStrategyTest {

  private AnimalShelterNotificationProducer producer1;
  private AnimalShelterNotificationProducer producer2;
  private NotificationProducerStrategy strategy;

  @BeforeEach
  void setUp() {
    producer1 = mock(AnimalShelterNotificationProducer.class);
    producer2 = mock(AnimalShelterNotificationProducer.class);

    when(producer1.getTopicId()).thenReturn("animal-info");
    when(producer2.getTopicId()).thenReturn("vaccination-info");

    strategy = new NotificationProducerStrategy(List.of(producer1, producer2));
  }

  @Test
  void shouldReturnCorrectServiceByTopic() {
    Optional<AnimalShelterNotificationProducer> result = strategy.getServiceByTopic("animal-info");
    assertTrue(result.isPresent());
    assertEquals(producer1, result.get());

    result = strategy.getServiceByTopic("vaccination-info");
    assertTrue(result.isPresent());
    assertEquals(producer2, result.get());
  }

  @Test
  void shouldReturnEmptyOptionalForUnknownTopic() {
    Optional<AnimalShelterNotificationProducer> result = strategy.getServiceByTopic(
        "unknown-topic");
    assertTrue(result.isEmpty());
  }

  @Test
  void shouldReturnAllServices() {
    List<AnimalShelterNotificationProducer> services = strategy.getAllServices();
    assertEquals(2, services.size());
    assertTrue(services.contains(producer1));
    assertTrue(services.contains(producer2));
  }
}
