package com.ansh.notification.subscription;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.ansh.event.subscription.SubscriptionDecisionEvent;
import com.ansh.service.AnimalTopicSubscriberRegistryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class SubscriberNotificationEventConsumerTest {

  @Mock
  private AnimalTopicSubscriberRegistryService topicSubscriberRegistryService;

  @InjectMocks
  private SubscriberNotificationEventConsumer subscriberNotificationEventConsumer;

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

    SubscriptionDecisionEvent expectedEvent = objectMapper.readValue(json,
        SubscriptionDecisionEvent.class);

    subscriberNotificationEventConsumer.listen(message);

    verify(topicSubscriberRegistryService, times(1)).handleSubscriptionApproval(
        expectedEvent.getEmail(),
        expectedEvent.getApprover(),
        expectedEvent.isReject()
    );
  }
}
