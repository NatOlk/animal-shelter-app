package com.ansh.notification.subscription;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.ansh.event.AnimalShelterTopic;
import com.ansh.event.subscription.SubscriptionDecisionEvent;
import com.ansh.service.SubscriberRegistryService;
import com.ansh.strategy.SubscriberRegistryServiceStrategy;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Optional;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SubscriberNotificationEventConsumerTest {

  private static final String ANIMAL_TOPIC = AnimalShelterTopic.ANIMAL_INFO.getTopicName();
  private static final String TEST_EMAIL = "test@test.com";
  private static final String APPROVER_EMAIL = "approver@test.com";

  private ObjectMapper objectMapper;
  private SubscriberRegistryServiceStrategy serviceStrategy;
  private SubscriberRegistryService mockService;
  private SubscriberNotificationEventConsumer consumer;

  @BeforeEach
  void setUp() {
    objectMapper = new ObjectMapper();
    serviceStrategy = mock(SubscriberRegistryServiceStrategy.class);
    mockService = mock(SubscriberRegistryService.class);

    consumer = new SubscriberNotificationEventConsumer();
    consumer.setSubscriberRegistryServiceStrategy(serviceStrategy);
  }

  @Test
  void shouldHandleSubscriptionWhenServiceExists() throws Exception {
    // given
    SubscriptionDecisionEvent event = SubscriptionDecisionEvent.builder()
        .topic(ANIMAL_TOPIC)
        .email(TEST_EMAIL)
        .approver(APPROVER_EMAIL)
        .reject(false)
        .build();

    String json = objectMapper.writeValueAsString(event);
    ConsumerRecord<String, String> record = new ConsumerRecord<>(ANIMAL_TOPIC, 0, 0L, "key", json);

    when(serviceStrategy.getServiceByTopic(ANIMAL_TOPIC)).thenReturn(Optional.of(mockService));

    // when
    consumer.listen(record);

    // then
    verify(mockService).handleSubscriptionApproval(TEST_EMAIL, APPROVER_EMAIL, false);
  }

  @Test
  void shouldLogWarningWhenNoServiceFound() throws Exception {
    // given
    SubscriptionDecisionEvent event = SubscriptionDecisionEvent.builder()
        .topic("unknown-topic")
        .email(TEST_EMAIL)
        .approver(APPROVER_EMAIL)
        .reject(true)
        .build();

    String json = objectMapper.writeValueAsString(event);
    ConsumerRecord<String, String> record = new ConsumerRecord<>("unknown-topic", 0, 0L, "key",
        json);

    when(serviceStrategy.getServiceByTopic("unknown-topic")).thenReturn(Optional.empty());

    // when
    consumer.listen(record);

    // then — no call
    verifyNoInteractions(mockService);
  }

  @Test
  void shouldThrowExceptionOnInvalidJson() {
    // given
    String invalidJson = "invalid json";
    ConsumerRecord<String, String> record = new ConsumerRecord<>(ANIMAL_TOPIC, 0, 0L, "key",
        invalidJson);

    // when / then
    try {
      consumer.listen(record);
      assert false : "Expected IOException to be thrown";
    } catch (IOException e) {
      // success
    }
  }
}
