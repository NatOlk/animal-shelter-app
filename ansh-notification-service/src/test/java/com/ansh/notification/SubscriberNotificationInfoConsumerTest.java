package com.ansh.notification;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.ansh.event.subscription.AnimalNotificationUserSubscribedEvent;
import com.ansh.service.AnimalTopicSubscriberRegistryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class SubscriberNotificationInfoConsumerTest {

  @Mock
  private AnimalTopicSubscriberRegistryService topicSubscriberRegistryService;

  @InjectMocks
  private SubscriberNotificationInfoConsumer subscriberNotificationInfoConsumer;

  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    objectMapper = new ObjectMapper();
  }

  @Test
  void testListen() throws Exception {
    String json = "{\"email\":\"test@example.com\", \"approver\":\"admin\", \"topic\":\"animal_notifications\", \"reject\":false}";
    ConsumerRecord<String, String> message = new ConsumerRecord<>("approveTopicId", 0, 0L, "key",
        json);

    AnimalNotificationUserSubscribedEvent expectedEvent = objectMapper.readValue(json,
        AnimalNotificationUserSubscribedEvent.class);

    subscriberNotificationInfoConsumer.listen(message);

    verify(topicSubscriberRegistryService, times(1)).handleSubscriptionApproval(
        expectedEvent.getEmail(),
        expectedEvent.getApprover(),
        expectedEvent.isReject()
    );
  }
}
