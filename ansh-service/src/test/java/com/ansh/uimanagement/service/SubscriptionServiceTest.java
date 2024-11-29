package com.ansh.uimanagement.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ansh.auth.service.UserProfileService;
import com.ansh.entity.animal.UserProfile;
import com.ansh.entity.animal.UserProfile.AnimalNotificationSubscriptionStatus;
import com.ansh.entity.subscription.Subscription;
import com.ansh.repository.PendingSubscriberRepository;
import com.ansh.repository.entity.PendingSubscriber;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

class SubscriptionServiceTest {

  @InjectMocks
  private SubscriptionService subscriptionService;

  @Mock
  private RestTemplate restTemplate;

  @Mock
  private PendingSubscriberRepository pendingSubscriberRepository;

  @Mock
  private UserProfileService userProfileService;

  @Captor
  private ArgumentCaptor<PendingSubscriber> subscriberCaptor;

  private final String animalShelterNotificationApp = "http://localhost:8081";
  private final String notificationApiKey = "test-key";

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    subscriptionService.setAnimalShelterNotificationApp(animalShelterNotificationApp);
    subscriptionService.setNotificationApiKey(notificationApiKey);
  }

  @Test
  void shouldSavePendingSubscriber() {
    String email = "test@example.com";
    String approver = "approver@example.com";
    String topic = "animalTopic";

    when(pendingSubscriberRepository.findByEmailAndTopic(email, topic)).thenReturn(Optional.empty());

    subscriptionService.savePendingSubscriber(email, approver, topic);

    verify(pendingSubscriberRepository).save(subscriberCaptor.capture());
    PendingSubscriber capturedSubscriber = subscriberCaptor.getValue();
    assertEquals(email, capturedSubscriber.getEmail());
    assertEquals(approver, capturedSubscriber.getApprover());
    assertEquals(topic, capturedSubscriber.getTopic());

    verify(userProfileService).updateAnimalNotificationSubscriptionStatus(
        email, UserProfile.AnimalNotificationSubscriptionStatus.PENDING);
  }

  @Test
  void shouldReturnPendingSubscribers() {
    String approver = "approver@example.com";
    PendingSubscriber subscriber1 = new PendingSubscriber();
    subscriber1.setEmail("email1@example.com");
    subscriber1.setApprover(approver);
    subscriber1.setTopic("topic1");

    PendingSubscriber subscriber2 = new PendingSubscriber();
    subscriber2.setEmail("email2@example.com");
    subscriber2.setApprover(approver);
    subscriber2.setTopic("topic2");

    List<PendingSubscriber> mockSubscribers = List.of(subscriber1, subscriber2);

    when(pendingSubscriberRepository.findByApprover(approver)).thenReturn(mockSubscribers);

    List<PendingSubscriber> result = subscriptionService.getPendingSubscribers(approver);

    assertEquals(2, result.size());
    verify(pendingSubscriberRepository).findByApprover(approver);
  }

  @Test
  void shouldReturnAllSubscriptionsByApprover() throws Exception {
    String approver = "approver@example.com";
    String url = animalShelterNotificationApp + "/internal/animal-notify-all-approver-subscriptions";

    Subscription s1 = new Subscription();
    s1.setId(1L);
    s1.setTopic("topic1");

    Subscription s2 = new Subscription();
    s2.setId(2L);
    s2.setTopic("topic2");

    List<Subscription> mockSubscriptions = List.of(s1, s2);
    String jsonBody = new ObjectMapper().writeValueAsString(Map.of("approver", approver));

    HttpHeaders headers = new HttpHeaders();
    headers.set("X-API-KEY", notificationApiKey);
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<String> expectedEntity = new HttpEntity<>(jsonBody, headers);
    ParameterizedTypeReference<List<Subscription>> typeRef = new ParameterizedTypeReference<>() {};
    ResponseEntity<List<Subscription>> mockResponse = new ResponseEntity<>(mockSubscriptions, HttpStatus.OK);

    when(restTemplate.exchange(eq(url), eq(HttpMethod.POST), eq(expectedEntity), eq(typeRef))).thenReturn(mockResponse);

    List<Subscription> result = subscriptionService.getAllSubscriptionByApprover(approver);

    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals("topic1", result.get(0).getTopic());
    assertEquals("topic2", result.get(1).getTopic());

    verify(restTemplate).exchange(eq(url), eq(HttpMethod.POST), eq(expectedEntity), eq(typeRef));
  }

  @Test
  void shouldReturnStatusByApprover() {
    String url = animalShelterNotificationApp + "/internal/animal-notify-approver-status";
    String expectedBody = "{\"approver\":\"approver@example.com\"}";
    HttpHeaders headers = new HttpHeaders();
    headers.add("X-API-KEY", notificationApiKey);
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<String> expectedEntity = new HttpEntity<>(expectedBody, headers);
    UserProfile.AnimalNotificationSubscriptionStatus mockResponse = AnimalNotificationSubscriptionStatus.ACTIVE;
    ParameterizedTypeReference<UserProfile.AnimalNotificationSubscriptionStatus> typeRef = new ParameterizedTypeReference<>() {};

    when(restTemplate.exchange(eq(url), eq(HttpMethod.POST), eq(expectedEntity), eq(typeRef))).thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

    UserProfile.AnimalNotificationSubscriptionStatus response = subscriptionService.getStatusByApprover("approver@example.com");

    assertNotNull(response);
    assertEquals(AnimalNotificationSubscriptionStatus.ACTIVE, response);
  }
}
