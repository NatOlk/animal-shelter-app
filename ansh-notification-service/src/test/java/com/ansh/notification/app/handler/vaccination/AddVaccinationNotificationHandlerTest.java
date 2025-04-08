package com.ansh.notification.app.handler.vaccination;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ansh.entity.animal.Animal;
import com.ansh.entity.subscription.Subscription;
import com.ansh.event.AddVaccinationEvent;
import com.ansh.event.AnimalEvent;
import com.ansh.event.RemoveAnimalEvent;
import com.ansh.service.AnimalTopicSubscriberRegistryService;
import com.ansh.service.EmailService;
import com.ansh.utils.LinkGenerator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class AddVaccinationNotificationHandlerTest {

  private static final String TEST_EMAIL = "test@example.com";
  private static final String MOCK_TOKEN = "mock-token";
  private static final String UNSUBSCRIBE_LINK_TEMPLATE = "http://example.com/unsubscribe/mock-token";
  private static final String ADD_VAX_SUBJECT = "[animal-shelter-app] New vaccine added";
  private static final String ADD_VAX_TEMPLATE = "addVaccineTemplate";
  private static final String ANIMAL_NAME = "Barsik";

  @InjectMocks
  private AddVaccinationNotificationHandler handler;

  @Mock
  private AnimalTopicSubscriberRegistryService animalTopicSubscriberRegistryService;

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

    when(animalTopicSubscriberRegistryService.getAcceptedAndApprovedSubscribers())
        .thenReturn(List.of(mockSubscription));
    when(linkGenerator.generateUnsubscribeLink(anyString()))
        .thenReturn(UNSUBSCRIBE_LINK_TEMPLATE);
  }

  @Test
  void testHandleNotification() {
    AnimalEvent event = new AddVaccinationEvent();
    Animal animal = new Animal();
    animal.setId(1L);
    animal.setName(ANIMAL_NAME);
    event.setAnimal(animal);

    handler.handle(event);

    ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);
    verify(emailNotificationExecutor, times(1)).execute(runnableCaptor.capture());

    runnableCaptor.getValue().run();

    ArgumentCaptor<Map<String, Object>> paramsCaptor = ArgumentCaptor.forClass(Map.class);

    verify(emailService, times(1)).sendEmail(
        eq(TEST_EMAIL),
        eq(ADD_VAX_SUBJECT),
        eq(ADD_VAX_TEMPLATE),
        paramsCaptor.capture()
    );

    Map<String, Object> capturedParams = paramsCaptor.getValue();
    assert capturedParams != null;
    assert capturedParams.get("name").equals(TEST_EMAIL);
    assert capturedParams.get("unsubscribeLink").equals(UNSUBSCRIBE_LINK_TEMPLATE);
    assert capturedParams.get("animalName").equals(ANIMAL_NAME);
    assertFalse(capturedParams.containsKey("confirmationLink"), "Confirmation link should not be present in captured params");
    assertFalse(capturedParams.containsKey("subscriptionLink"), "Subscription link should not be present in captured params");
  }

  @Test
  void testThrowException_whenWrongEventType() {
    AnimalEvent event = new RemoveAnimalEvent();
    Animal animal = new Animal();
    animal.setId(1L);
    animal.setName(ANIMAL_NAME);
    event.setAnimal(animal);

    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      handler.handle(event);
    });

    assertNotNull(exception);
    assertEquals("Invalid event type for handler: RemoveAnimalEvent", exception.getMessage());
  }
}
