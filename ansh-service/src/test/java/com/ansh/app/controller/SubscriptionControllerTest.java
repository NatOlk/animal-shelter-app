package com.ansh.app.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.ansh.app.facade.SubscriptionFacade;
import com.ansh.dto.NotificationStatusDTO;
import com.ansh.dto.SubscriptionRequest;
import com.ansh.entity.account.UserProfile.AnimalInfoNotifStatus;
import com.ansh.entity.subscription.Subscription;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.context.request.async.DeferredResult;

class SubscriptionControllerTest {

  private static final String APPROVER_EMAIL = "admin@example.com";
  private static final String TEST_EMAIL = "test@example.com";
  private static final String TOPIC_ID = "animalTopicId";

  @Mock
  private SubscriptionFacade subscriptionFacade;
  @InjectMocks
  private SubscriptionController subscriptionController;

  private SubscriptionRequest req;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    req = SubscriptionRequest.builder()
        .approver(APPROVER_EMAIL)
        .email(TEST_EMAIL)
        .topic(TOPIC_ID)
        .build();
  }


  @Test
  void shouldReturnSubscribers() throws InterruptedException {
    List<Subscription> subscriptions = List.of(new Subscription());
    DeferredResult<List<Subscription>> deferredResult = new DeferredResult<>();
    deferredResult.setResult(subscriptions);

    when(subscriptionFacade.getAllSubscribers(any())).thenReturn(deferredResult);

    DeferredResult<List<Subscription>> result = subscriptionController.getSubscribers(req);

    CountDownLatch latch = new CountDownLatch(1);
    result.onCompletion(latch::countDown);
    latch.await(1, TimeUnit.SECONDS);

    assertNotNull(result.getResult());
    assertEquals(subscriptions, result.getResult());
  }

  @Test
  void shouldReturnApproverStatus() throws InterruptedException {
    NotificationStatusDTO expectedStatus = new NotificationStatusDTO(
        AnimalInfoNotifStatus.ACTIVE,
        AnimalInfoNotifStatus.PENDING,
        AnimalInfoNotifStatus.NONE
    );

    DeferredResult<NotificationStatusDTO> deferredResult = new DeferredResult<>();
    deferredResult.setResult(expectedStatus);

    when(subscriptionFacade.getNotificationStatusesByAccount(any())).thenReturn(deferredResult);

    DeferredResult<NotificationStatusDTO> result = subscriptionController.getStatuses(req);

    CountDownLatch latch = new CountDownLatch(1);
    result.onCompletion(latch::countDown);
    latch.await(1, TimeUnit.SECONDS);

    assertNotNull(result.getResult());
    assertEquals(expectedStatus, result.getResult());
  }
}
