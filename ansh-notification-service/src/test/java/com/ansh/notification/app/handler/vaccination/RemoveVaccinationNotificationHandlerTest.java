package com.ansh.notification.app.handler.vaccination;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ansh.entity.animal.Animal;
import com.ansh.entity.subscription.Subscription;
import com.ansh.event.AnimalShelterEvent;
import com.ansh.event.AnimalShelterTopic;
import com.ansh.event.animal.AddAnimalEvent;
import com.ansh.event.vaccination.RemoveVaccinationEvent;
import com.ansh.notification.NotificationMessages;
import com.ansh.service.EmailService;
import com.ansh.service.SubscriberRegistryService;
import com.ansh.strategy.SubscriberRegistryServiceStrategy;
import com.ansh.utils.LinkGenerator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class RemoveVaccinationNotificationHandlerTest {

  private static final String TEST_EMAIL = "test@example.com";
  private static final String MOCK_TOKEN = "mock-token";
  private static final String UNSUBSCRIBE_LINK = "http://example.com/unsubscribe/mock-token";
  private static final String ANIMAL_NAME = "Barsik";

  @InjectMocks
  private RemoveVaccinationNotificationHandler handler;

  @Mock
  private SubscriberRegistryServiceStrategy subscriberRegistryServiceStrategy;

  @Mock
  private SubscriberRegistryService vaccinationTopicSubscriber;

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

    when(vaccinationTopicSubscriber.getAcceptedAndApprovedSubscribers())
        .thenReturn(List.of(mockSubscription));
    when(linkGenerator.generateUnsubscribeLink(anyString()))
        .thenReturn(UNSUBSCRIBE_LINK);
    when(subscriberRegistryServiceStrategy
        .getServiceByTopic(AnimalShelterTopic.VACCINATION_INFO.getTopicName()))
        .thenReturn(Optional.of(vaccinationTopicSubscriber));
  }

  @Test
  void shouldSendEmailToVaccinationSubscribers() {
    // given
    Animal animal = new Animal();
    animal.setId(1L);
    animal.setName(ANIMAL_NAME);

    AnimalShelterEvent event = new RemoveVaccinationEvent();
    event.setAnimal(animal);

    ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);
    ArgumentCaptor<Map<String, Object>> paramsCaptor = ArgumentCaptor.forClass(Map.class);

    // when
    handler.handle(AnimalShelterTopic.VACCINATION_INFO.getTopicName(), event);

    // then
    verify(emailNotificationExecutor, times(1)).execute(runnableCaptor.capture());
    runnableCaptor.getValue().run();

    verify(emailService, times(1)).sendEmail(
        eq(TEST_EMAIL),
        eq(NotificationMessages.REMOVE_VACCINE_SUBJECT),
        eq(NotificationMessages.REMOVE_VACCINE_TEMPLATE),
        paramsCaptor.capture()
    );

    Map<String, Object> capturedParams = paramsCaptor.getValue();
    assertNotNull(capturedParams);
    assertEquals(TEST_EMAIL, capturedParams.get("name"));
    assertEquals(UNSUBSCRIBE_LINK, capturedParams.get("unsubscribeLink"));
    assertEquals(ANIMAL_NAME, capturedParams.get("animalName"));
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
        () -> handler.handle(AnimalShelterTopic.VACCINATION_INFO.getTopicName(), event));

    assertNotNull(exception);
    assertEquals("Invalid event type for handler: AddAnimalEvent", exception.getMessage());
  }
}