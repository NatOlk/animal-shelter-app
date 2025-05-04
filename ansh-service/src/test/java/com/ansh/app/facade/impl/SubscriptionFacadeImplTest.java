package com.ansh.app.facade.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import com.ansh.app.service.notification.subscription.NotificationSubscriptionService;
import com.ansh.app.service.user.UserProfileService;
import com.ansh.dto.SubscriptionRequest;
import com.ansh.entity.account.UserProfile.AnimalInfoNotifStatus;
import com.ansh.entity.subscription.Subscription;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import reactor.core.publisher.Mono;

class SubscriptionFacadeImplTest {

  @InjectMocks
  private SubscriptionFacadeImpl subscriptionFacade;

  @Mock
  private NotificationSubscriptionService notificationService;

  @Mock
  private UserProfileService userProfileService;

  private SubscriptionRequest request;

  @BeforeEach
  void setUp() {
    openMocks(this);
    request = new SubscriptionRequest();
    request.setApprover("admin@example.com");
  }

  @Test
  void shouldReturnActiveStatus() {
    when(notificationService.getAnimalInfoStatusByAccount(anyString()))
        .thenReturn(Mono.just(AnimalInfoNotifStatus.ACTIVE));

    var result = subscriptionFacade.getApproverStatus(request);
    result.onCompletion(() ->
        assertEquals(AnimalInfoNotifStatus.ACTIVE, result.getResult())
    );
  }

  @Test
  void shouldReturnUnknownStatusOnError() {
    when(notificationService.getAnimalInfoStatusByAccount(anyString()))
        .thenReturn(Mono.error(new RuntimeException()));

    var result = subscriptionFacade.getApproverStatus(request);
    result.onCompletion(() ->
        assertEquals(AnimalInfoNotifStatus.UNKNOWN, result.getResult())
    );
  }

  @Test
  void shouldReturnNoneStatusIfApproverIsEmpty() {
    request.setApprover(null);

    var result = subscriptionFacade.getApproverStatus(request);
    assertEquals(AnimalInfoNotifStatus.NONE, result.getResult());
  }

  @Test
  void shouldReturnAllSubscribers() {
    List<Subscription> subscriptions = Collections.singletonList(new Subscription());
    when(notificationService.getAllSubscriptionByApprover(anyString()))
        .thenReturn(Mono.just(subscriptions));

    var result = subscriptionFacade.getAllSubscribers(request);
    result.onCompletion(() ->
        assertEquals(subscriptions, result.getResult())
    );
  }

  @Test
  void shouldReturnEmptyListOnError() {
    when(notificationService.getAllSubscriptionByApprover(anyString()))
        .thenReturn(Mono.error(new RuntimeException()));

    var result = subscriptionFacade.getAllSubscribers(request);
    result.onCompletion(() ->
        assertEquals(Collections.emptyList(), result.getResult())
    );
  }
}
