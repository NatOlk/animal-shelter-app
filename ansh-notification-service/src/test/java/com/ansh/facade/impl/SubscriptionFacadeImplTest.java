package com.ansh.facade.impl;

import com.ansh.dto.NotificationStatusDTO;
import com.ansh.dto.SubscriptionRequest;
import com.ansh.entity.account.UserProfile.AnimalInfoNotifStatus;
import com.ansh.entity.subscription.Subscription;
import com.ansh.event.AnimalShelterTopic;
import com.ansh.service.SubscriberRegistryService;
import com.ansh.strategy.SubscriberRegistryServiceStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SubscriptionFacadeImplTest {

  @Mock
  private SubscriberRegistryServiceStrategy strategy;

  @Mock
  private SubscriberRegistryService mockService;

  @InjectMocks
  private SubscriptionFacadeImpl facade;

  private final String TEST_EMAIL = "test@test.com";
  private final String APPROVER = "admin@test.com";
  private final String TOPIC = AnimalShelterTopic.ANIMAL_INFO.getTopicName();
  private final String TOKEN = "some-token";

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testRegisterSubscription_success() {
    SubscriptionRequest req = new SubscriptionRequest(TEST_EMAIL, APPROVER, TOPIC);

    when(strategy.getServiceByTopic(TOPIC)).thenReturn(Optional.of(mockService));

    facade.registerSubscription(req);

    verify(mockService).registerSubscriber(TEST_EMAIL, APPROVER);
  }

  @Test
  void testRegisterSubscription_serviceNotFound() {
    SubscriptionRequest req = new SubscriptionRequest(TEST_EMAIL, APPROVER, "bad-topic");

    when(strategy.getServiceByTopic("bad-topic")).thenReturn(Optional.empty());

    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
        () -> facade.registerSubscription(req));

    assertTrue(ex.getMessage().contains("No service found for topic"));
  }

  @Test
  void testUnsubscribe_withToken() {
    when(strategy.getAllServices()).thenReturn(List.of(mockService));

    facade.unsubscribe(TOKEN);

    verify(mockService).unsubscribe(TOKEN);
  }

  @Test
  void testUnsubscribe_withRequest() {
    SubscriptionRequest req = new SubscriptionRequest(TEST_EMAIL, APPROVER, TOPIC);
    when(strategy.getServiceByTopic(TOPIC)).thenReturn(Optional.of(mockService));

    facade.unsubscribe(req);

    verify(mockService).unsubscribe(TEST_EMAIL, APPROVER);
  }

  @Test
  void testGetAllSubscriptionByAccount() {
    Subscription sub = new Subscription();
    when(mockService.getAllSubscriptions(APPROVER)).thenReturn(List.of(sub));
    when(strategy.getAllServices()).thenReturn(List.of(mockService));

    List<Subscription> result = facade.getAllSubscriptionByAccount(APPROVER);

    assertEquals(1, result.size());
    assertSame(sub, result.get(0));
  }

  @Test
  void testGetSubscriptionStatuses_returnsCorrectDTO() {
    when(strategy.getServiceByTopic(TOPIC)).thenReturn(Optional.of(mockService));
    when(mockService.getSubscriptionStatus(APPROVER)).thenReturn(AnimalInfoNotifStatus.ACTIVE);

    NotificationStatusDTO dto = facade.getSubscriptionStatuses(APPROVER);

    assertEquals(AnimalInfoNotifStatus.ACTIVE, dto.animalTopicId());
    assertEquals(AnimalInfoNotifStatus.NONE, dto.animalShelterNewsTopicId());
    assertEquals(AnimalInfoNotifStatus.NONE, dto.vaccinationTopicId());
  }

  @Test
  void testGetSubscriptionStatuses_defaultToNoneIfServiceNotFound() {
    when(strategy.getServiceByTopic(TOPIC)).thenReturn(Optional.empty());

    NotificationStatusDTO dto = facade.getSubscriptionStatuses(APPROVER);

    assertEquals(AnimalInfoNotifStatus.NONE, dto.animalTopicId());
    assertEquals(AnimalInfoNotifStatus.NONE, dto.animalShelterNewsTopicId());
    assertEquals(AnimalInfoNotifStatus.NONE, dto.vaccinationTopicId());
  }

  @Test
  void testHandleSubscriptionApproval_success() {
    when(strategy.getServiceByTopic(TOPIC)).thenReturn(Optional.of(mockService));

    facade.handleSubscriptionApproval(TEST_EMAIL, APPROVER, TOPIC, true);

    verify(mockService).handleSubscriptionApproval(TEST_EMAIL, APPROVER, true);
  }

  @Test
  void testHandleSubscriptionApproval_serviceNotFound() {
    when(strategy.getServiceByTopic("bad-topic")).thenReturn(Optional.empty());

    assertThrows(IllegalArgumentException.class, () ->
        facade.handleSubscriptionApproval(TEST_EMAIL, APPROVER, "bad-topic", false));
  }
}