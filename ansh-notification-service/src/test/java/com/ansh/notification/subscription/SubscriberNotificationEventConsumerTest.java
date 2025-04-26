package com.ansh.notification.subscription;

import static org.mockito.Mockito.*;

import com.ansh.event.subscription.SubscriptionDecisionEvent;
import com.ansh.service.SubscriberRegistryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class SubscriberNotificationEventConsumerTest {

  private SubscriberRegistryService animalTopicSubscriber;
  private SubscriberRegistryService vaccinationTopicSubscriber;
  private SubscriberNotificationEventConsumer consumer;
  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    objectMapper = new ObjectMapper();

    animalTopicSubscriber = mock(SubscriberRegistryService.class);
    vaccinationTopicSubscriber = mock(SubscriberRegistryService.class);

    when(animalTopicSubscriber.getTopicId()).thenReturn("animal-topic");
    when(vaccinationTopicSubscriber.getTopicId()).thenReturn("vaccination-topic");

    consumer = new SubscriberNotificationEventConsumer(List.of(animalTopicSubscriber,
        vaccinationTopicSubscriber));
  }

  @Test
  void shouldProcessAnimalTopicSuccessfully() throws Exception {
    SubscriptionDecisionEvent event = SubscriptionDecisionEvent.builder()
        .email("test@example.com")
        .approver("admin@example.com")
        .topic("animal-topic")
        .reject(false)
        .build();

    String json = objectMapper.writeValueAsString(event);
    ConsumerRecord<String, String> record = new ConsumerRecord<>("animal-topic", 0, 0L, "key",
        json);

    consumer.listen(record);

    verify(animalTopicSubscriber, times(1)).handleSubscriptionApproval(
        "test@example.com",
        "admin@example.com",
        false
    );
    verify(vaccinationTopicSubscriber, times(0)).handleSubscriptionApproval(
        "test@example.com",
        "admin@example.com",
        false
    );
  }

  @Test
  void shouldProcessVaccinationTopicSuccessfully() throws Exception {
    SubscriptionDecisionEvent event = SubscriptionDecisionEvent.builder()
        .email("doc@example.com")
        .approver("vet@example.com")
        .topic("vaccination-topic")
        .reject(true)
        .build();

    String json = objectMapper.writeValueAsString(event);
    ConsumerRecord<String, String> record = new ConsumerRecord<>("vaccination-topic", 0, 0L, "key",
        json);

    consumer.listen(record);

    verify(vaccinationTopicSubscriber, times(1)).handleSubscriptionApproval(
        "doc@example.com",
        "vet@example.com",
        true
    );
    verify(animalTopicSubscriber, times(0)).handleSubscriptionApproval(
        "test@example.com",
        "admin@example.com",
        false
    );
  }

  @Test
  void shouldDoNothing_WhenNoMatchingServiceFound() throws Exception {
    SubscriptionDecisionEvent event = SubscriptionDecisionEvent.builder()
        .email("user@example.com")
        .approver("admin@example.com")
        .topic("unknown-topic")
        .reject(false)
        .build();

    String json = objectMapper.writeValueAsString(event);
    ConsumerRecord<String, String> record = new ConsumerRecord<>("unknown-topic", 0, 0L, "key",
        json);

    consumer.listen(record);

    verify(vaccinationTopicSubscriber, times(0)).handleSubscriptionApproval(
        "doc@example.com",
        "vet@example.com",
        true
    );
    verify(animalTopicSubscriber, times(0)).handleSubscriptionApproval(
        "test@example.com",
        "admin@example.com",
        false
    );
  }

  @Test
  void shouldThrowException_WhenJsonInvalid() {
    String invalidJson = "this is not a valid json";
    ConsumerRecord<String, String> record = new ConsumerRecord<>("animal-topic", 0, 0L, "key",
        invalidJson);

    Exception thrown = null;
    try {
      consumer.listen(record);
    } catch (Exception e) {
      thrown = e;
    }

    assert thrown != null;
    assert thrown instanceof com.fasterxml.jackson.core.JsonProcessingException;
  }
}
