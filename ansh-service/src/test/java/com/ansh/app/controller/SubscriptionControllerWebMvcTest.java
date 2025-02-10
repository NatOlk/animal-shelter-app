package com.ansh.app.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ansh.AnshSecurityConfig;
import com.ansh.app.service.notification.subscription.AnimalInfoPendingSubscriptionService;
import com.ansh.app.service.notification.subscription.NotificationSubscriptionService;
import com.ansh.entity.animal.UserProfile.AnimalNotificationSubscriptionStatus;
import com.ansh.entity.subscription.Subscription;
import com.ansh.repository.entity.PendingSubscriber;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.request.async.DeferredResult;
import reactor.core.publisher.Mono;

@WebMvcTest(SubscriptionController.class)
@Import(AnshSecurityConfig.class)
class SubscriptionControllerWebMvcTest extends AbstractControllerWebMvcTest {

  private static final String AUTH_HEADER = HttpHeaders.AUTHORIZATION;
  private static final String APPROVER_EMAIL = "admin@example.com";
  private static final String TEST_EMAIL = "test@example.com";

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  @Qualifier("notificationSubscriptionService")
  private NotificationSubscriptionService notificationService;

  @MockBean
  @Qualifier("animalInfoPendingSubscriptionService")
  private AnimalInfoPendingSubscriptionService pendingSubscriptionService;

  @Test
  void shouldApproveSubscriber() throws Exception {
    mockMvc.perform(post("/animal-notify-approve-subscriber")
            .header(AUTH_HEADER, BEARER_TOKEN)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"email\":\"" + TEST_EMAIL + "\",\"approver\":\"" + APPROVER_EMAIL + "\"}"))
        .andExpect(status().isOk());
  }

  @Test
  void shouldRejectSubscriber() throws Exception {
    mockMvc.perform(post("/animal-notify-reject-subscriber")
            .header(AUTH_HEADER, BEARER_TOKEN)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"email\":\"" + TEST_EMAIL + "\"}"))
        .andExpect(status().isOk());
  }

  @Test
  void shouldReturnPendingSubscribers() throws Exception {
    PendingSubscriber mockSubscriber = new PendingSubscriber();
    mockSubscriber.setEmail(USERNAME);
    when(pendingSubscriptionService.getSubscribersByApprover(eq(APPROVER_EMAIL)))
        .thenReturn(List.of(mockSubscriber));

    mockMvc.perform(post("/animal-notify-pending-subscribers")
            .header(AUTH_HEADER, BEARER_TOKEN)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"approver\":\"" + APPROVER_EMAIL + "\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].email").value(USERNAME));
  }

  @Test
  void shouldReturnPendingNoApproverSubscribers() throws Exception {
    when(pendingSubscriptionService.getPendingSubscribersWithoutApprover())
        .thenReturn(Collections.emptyList());

    mockMvc.perform(get("/animal-notify-pending-no-approver-subscribers")
            .header(AUTH_HEADER, BEARER_TOKEN))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isEmpty());
  }

  @Test
  void shouldReturnSubscribers() throws Exception {
    Subscription mockSubscription = new Subscription();
    mockSubscription.setApprover(APPROVER_EMAIL);
    List<Subscription> subscriptions = List.of(mockSubscription);

    DeferredResult<ResponseEntity<List<Subscription>>> deferredResult = new DeferredResult<>();
    deferredResult.setResult(ResponseEntity.ok(subscriptions));

    when(notificationService.getAllSubscriptionByApprover(eq(APPROVER_EMAIL)))
        .thenReturn(Mono.just(subscriptions));

    MvcResult mvcResult = mockMvc.perform(post("/animal-notify-all-approver-subscriptions")
            .header(AUTH_HEADER, BEARER_TOKEN)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"approver\":\"" + APPROVER_EMAIL + "\"}"))
        .andReturn();
    assertEquals(mvcResult.getAsyncResult(), subscriptions);
  }

  @Test
  void shouldReturnApproverStatus() throws Exception {
    AnimalNotificationSubscriptionStatus mockStatus = AnimalNotificationSubscriptionStatus.ACTIVE;

    DeferredResult<ResponseEntity<AnimalNotificationSubscriptionStatus>> deferredResult = new DeferredResult<>();
    deferredResult.setResult(ResponseEntity.ok(mockStatus));

    when(notificationService.getStatusByApprover(eq(APPROVER_EMAIL)))
        .thenReturn(Mono.just(mockStatus));

    MvcResult mvcResult = mockMvc.perform(post("/animal-notify-approver-status")
            .header(AUTH_HEADER, BEARER_TOKEN)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"approver\":\"" + APPROVER_EMAIL + "\"}"))
        .andReturn();
    assertEquals(mvcResult.getAsyncResult(), mockStatus);
  }

  @Test
  void shouldReturnEmptyForInvalidApproverStatusRequest() throws Exception {
    MvcResult mvcResult = mockMvc.perform(post("/animal-notify-approver-status")
            .header(AUTH_HEADER, BEARER_TOKEN)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{}"))
        .andReturn();
    assertEquals(mvcResult.getAsyncResult(), AnimalNotificationSubscriptionStatus.NONE);
  }
}
