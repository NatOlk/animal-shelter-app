package com.ansh.notification.app.handler.animal;

import static com.ansh.event.AnimalShelterTopic.ANIMAL_INFO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.ansh.entity.animal.Animal;
import com.ansh.entity.subscription.Subscription;
import com.ansh.event.AnimalShelterEvent;
import com.ansh.event.animal.AddAnimalEvent;
import com.ansh.event.animal.RemoveAnimalEvent;
import com.ansh.notification.NotificationMessages;
import com.ansh.service.EmailService;
import com.ansh.service.SubscriberRegistryService;
import com.ansh.strategy.SubscriberRegistryServiceStrategy;
import com.ansh.utils.LinkGenerator;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

class RemoveAnimalNotificationHandlerTest {

  private static final String TEST_EMAIL = "test@example.com";
  private static final String MOCK_TOKEN = "mock-token";
  private static final String UNSUBSCRIBE_LINK = "http://example.com/unsubscribe/mock-token";
  private static final String ANIMAL_NAME = "Barsik";

  @InjectMocks
  private RemoveAnimalNotificationHandler handler;

  @Mock
  private SubscriberRegistryServiceStrategy subscriberRegistryServiceStrategy;

  @Mock
  private SubscriberRegistryService animalTopicSubscriber;

  @Mock
  private SubscriberRegistryService animalShelterNewsSubscriber;

  @Mock
  private EmailService emailService;

  @Mock
  private LinkGenerator linkGenerator;

  @Mock
  private Executor emailNotificationExecutor;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    // given
    Subscription mockSubscription = new Subscription();
    mockSubscription.setEmail(TEST_EMAIL);
    mockSubscription.setToken(MOCK_TOKEN);

    when(animalTopicSubscriber.getAcceptedAndApprovedSubscribers())
        .thenReturn(List.of(mockSubscription));
    when(animalShelterNewsSubscriber.getAcceptedAndApprovedSubscribers())
        .thenReturn(List.of(mockSubscription));
    when(linkGenerator.generateUnsubscribeLink(anyString()))
        .thenReturn(UNSUBSCRIBE_LINK);

    when(subscriberRegistryServiceStrategy
        .getServiceByTopic(ANIMAL_INFO.getTopicName()))
        .thenReturn(Optional.of(animalTopicSubscriber));
  }

  @Test
  void shouldSendEmailToAllSubscribers() {
    // given
    Animal animal = new Animal();
    animal.setId(1L);
    animal.setName(ANIMAL_NAME);

    AnimalShelterEvent event = new RemoveAnimalEvent();
    event.setAnimal(animal);

    ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);
    ArgumentCaptor<Map<String, Object>> paramsCaptor = ArgumentCaptor.forClass(Map.class);

    // when
    handler.handle(ANIMAL_INFO.getTopicName(), event);

    // then
    verify(emailNotificationExecutor, times(1)).execute(runnableCaptor.capture());

    runnableCaptor.getAllValues().forEach(Runnable::run);

    verify(emailService, times(1)).sendEmail(
        eq(TEST_EMAIL),
        eq(NotificationMessages.REMOVE_ANIMAL_SUBJECT),
        eq(NotificationMessages.REMOVE_ANIMAL_TEMPLATE),
        paramsCaptor.capture()
    );

    List<Map<String, Object>> capturedParamsList = paramsCaptor.getAllValues();
    for (Map<String, Object> params : capturedParamsList) {
      assertNotNull(params);
      assertEquals(TEST_EMAIL, params.get("name"));
      assertEquals(UNSUBSCRIBE_LINK, params.get("unsubscribeLink"));
      assertEquals(ANIMAL_NAME, params.get("animalName"));
    }
  }

  @Test
  void shouldThrowException_WhenWrongEventType() {
    // given
    Animal animal = new Animal();
    animal.setId(1L);
    animal.setName(ANIMAL_NAME);

    AnimalShelterEvent event = new AddAnimalEvent();
    event.setAnimal(animal);

    // when / then
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> handler.handle(ANIMAL_INFO.getTopicName(), event));

    assertNotNull(exception);
    assertEquals("Invalid event type for handler: AddAnimalEvent", exception.getMessage());
  }
}