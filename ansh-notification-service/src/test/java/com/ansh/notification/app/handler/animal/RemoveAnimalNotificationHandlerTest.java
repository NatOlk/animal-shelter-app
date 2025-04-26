package com.ansh.notification.app.handler.animal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.ansh.entity.animal.Animal;
import com.ansh.entity.subscription.Subscription;
import com.ansh.event.AnimalEvent;
import com.ansh.event.AddAnimalEvent;
import com.ansh.event.RemoveAnimalEvent;
import com.ansh.notification.NotificationMessages;
import com.ansh.service.EmailService;
import com.ansh.service.SubscriberRegistryService;
import com.ansh.utils.LinkGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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

    Subscription mockSubscription = new Subscription();
    mockSubscription.setEmail(TEST_EMAIL);
    mockSubscription.setToken(MOCK_TOKEN);

    when(animalTopicSubscriber.getAcceptedAndApprovedSubscribers()).thenReturn(List.of(mockSubscription));
    when(animalShelterNewsSubscriber.getAcceptedAndApprovedSubscribers()).thenReturn(List.of(mockSubscription));
    when(linkGenerator.generateUnsubscribeLink(anyString())).thenReturn(UNSUBSCRIBE_LINK);
  }

  @Test
  void shouldSendEmailToAllSubscribers() {
    AnimalEvent event = new RemoveAnimalEvent();
    Animal animal = new Animal();
    animal.setId(1L);
    animal.setName(ANIMAL_NAME);
    event.setAnimal(animal);

    handler.handle(event);

    ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);
    verify(emailNotificationExecutor, times(2)).execute(runnableCaptor.capture());

    runnableCaptor.getAllValues().forEach(Runnable::run);

    ArgumentCaptor<Map<String, Object>> paramsCaptor = ArgumentCaptor.forClass(Map.class);

    verify(emailService, times(2)).sendEmail(
        eq(TEST_EMAIL),
        eq(NotificationMessages.REMOVE_ANIMAL_SUBJECT),
        eq(NotificationMessages.REMOVE_ANIMAL_TEMPLATE),
        paramsCaptor.capture()
    );

    List<Map<String, Object>> capturedParamsList = paramsCaptor.getAllValues();
    for (Map<String, Object> capturedParams : capturedParamsList) {
      assertNotNull(capturedParams);
      assertEquals(TEST_EMAIL, capturedParams.get("name"));
      assertEquals(UNSUBSCRIBE_LINK, capturedParams.get("unsubscribeLink"));
      assertEquals(ANIMAL_NAME, capturedParams.get("animalName"));
    }
  }

  @Test
  void shouldThrowException_WhenWrongEventType() {
    AnimalEvent event = new AddAnimalEvent();
    Animal animal = new Animal();
    animal.setId(1L);
    animal.setName(ANIMAL_NAME);
    event.setAnimal(animal);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> handler.handle(event));

    assertNotNull(exception);
    assertEquals("Invalid event type for handler: AddAnimalEvent", exception.getMessage());
  }
}
