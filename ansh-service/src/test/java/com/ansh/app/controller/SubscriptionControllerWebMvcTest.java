package com.ansh.app.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ansh.AnshSecurityConfig;
import com.ansh.app.service.notification.subscription.NotificationSubscriptionService;
import com.ansh.app.service.notification.subscription.PendingSubscriptionService;
import com.ansh.entity.account.UserProfile.AnimalInfoNotifStatus;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import reactor.core.publisher.Mono;

@WebMvcTest(SubscriptionController.class)
@Import(AnshSecurityConfig.class)
class SubscriptionControllerWebMvcTest extends AbstractControllerWebMvcTest {

  private static final String APPROVER_EMAIL = "admin@example.com";
  private static final String TEST_EMAIL = "test@example.com";

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  @Qualifier("notificationSubscriptionService")
  private NotificationSubscriptionService notificationService;

  @MockBean
  @Qualifier("animalInfoPendingSubscriptionService")
  private PendingSubscriptionService pendingSubscriptionService;

  @Test
  void shouldApproveSubscriber() throws Exception {
    mockMvc.perform(post("/api/animal-notify-approve-subscriber")
            .header(AUTH_HEADER, BEARER_TOKEN)
            .contentType(MediaType.APPLICATION_JSON)
            .content(STR."{\"email\":\"\{TEST_EMAIL}\",\"approver\":\"\{APPROVER_EMAIL}\"}"))
        .andExpect(status().isOk());
  }

  @Test
  void shouldRejectSubscriber() throws Exception {
    mockMvc.perform(post("/api/animal-notify-reject-subscriber")
            .header(AUTH_HEADER, BEARER_TOKEN)
            .contentType(MediaType.APPLICATION_JSON)
            .content(STR."{\"email\":\"\{TEST_EMAIL}\"}"))
        .andExpect(status().isOk());
  }

  @Test
  void shouldReturnPendingSubscribers() throws Exception {
    PendingSubscriber mockSubscriber = new PendingSubscriber();
    mockSubscriber.setEmail(TEST_EMAIL);
    when(pendingSubscriptionService.getSubscribersByApprover(eq(APPROVER_EMAIL)))
        .thenReturn(List.of(mockSubscriber));

    mockMvc.perform(post("/api/animal-notify-pending-subscribers")
            .header(AUTH_HEADER, BEARER_TOKEN)
            .contentType(MediaType.APPLICATION_JSON)
            .content(STR."{\"approver\":\"\{APPROVER_EMAIL}\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].email").value(TEST_EMAIL));
  }

  @Test
  void shouldReturnPendingNoApproverSubscribers() throws Exception {
    when(pendingSubscriptionService.getPendingSubscribersWithoutApprover())
        .thenReturn(Collections.emptyList());

    mockMvc.perform(get("/api/animal-notify-pending-no-approver-subscribers")
            .header(AUTH_HEADER, BEARER_TOKEN))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isEmpty());
  }

  @Test
  void shouldReturnSubscribers() throws Exception {
    Subscription mockSubscription = new Subscription();
    mockSubscription.setApprover(APPROVER_EMAIL);
    List<Subscription> subscriptions = List.of(mockSubscription);

    when(notificationService.getAllSubscriptionByApprover(eq(APPROVER_EMAIL)))
        .thenReturn(Mono.just(subscriptions));

    MvcResult mvcResult = mockMvc.perform(post("/api/animal-notify-all-approver-subscriptions")
            .header(AUTH_HEADER, BEARER_TOKEN)
            .contentType(MediaType.APPLICATION_JSON)
            .content(STR."{\"approver\":\"\{APPROVER_EMAIL}\"}"))
        .andReturn();
    assertEquals(mvcResult.getAsyncResult(), subscriptions);
  }

  @Test
  void shouldReturnApproverStatus() throws Exception {
    when(notificationService.getAnimalInfoStatusByApprover(eq(APPROVER_EMAIL)))
        .thenReturn(Mono.just(AnimalInfoNotifStatus.ACTIVE));

    MvcResult mvcResult = mockMvc.perform(post("/api/animal-notify-approver-status")
            .header(AUTH_HEADER, BEARER_TOKEN)
            .contentType(MediaType.APPLICATION_JSON)
            .content(STR."{\"approver\":\"\{APPROVER_EMAIL}\"}"))
        .andReturn();

    assertEquals(mvcResult.getAsyncResult(), AnimalInfoNotifStatus.ACTIVE);
  }

  @Test
  void shouldReturnUnknownStatus_whenServiceFails() throws Exception {
    when(notificationService.getAnimalInfoStatusByApprover(eq(APPROVER_EMAIL)))
        .thenReturn(Mono.error(new RuntimeException("Service unavailable")));

    MvcResult mvcResult = mockMvc.perform(post("/api/animal-notify-approver-status")
            .header(AUTH_HEADER, BEARER_TOKEN)
            .contentType(MediaType.APPLICATION_JSON)
            .content(STR."{\"approver\":\"\{APPROVER_EMAIL}\"}"))
        .andReturn();

    assertEquals(mvcResult.getAsyncResult(), AnimalInfoNotifStatus.UNKNOWN);
  }

  @Test
  void shouldReturnEmptyList_whenServiceFails() throws Exception {
    when(notificationService.getAllSubscriptionByApprover(eq(APPROVER_EMAIL)))
        .thenReturn(Mono.error(new RuntimeException("Service unavailable")));

    MvcResult mvcResult = mockMvc.perform(post("/api/animal-notify-all-approver-subscriptions")
            .header(AUTH_HEADER, BEARER_TOKEN)
            .contentType(MediaType.APPLICATION_JSON)
            .content(STR."{\"approver\":\"\{APPROVER_EMAIL}\"}"))
        .andReturn();

    assertEquals(mvcResult.getAsyncResult(), Collections.emptyList());
  }

  @Test
  void shouldReturnBadRequestForInvalidRequest() throws Exception {
    mockMvc.perform(post("/api/animal-notify-approver-status")
            .header(AUTH_HEADER, BEARER_TOKEN)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{invalid-json}"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturnUnauthorized_whenNoAuthHeader() throws Exception {
    mockMvc.perform(post("/api/animal-notify-approver-status")
            .contentType(MediaType.APPLICATION_JSON)
            .content(STR."{\"approver\":\"\{APPROVER_EMAIL}\"}"))
        .andExpect(status().isUnauthorized());
  }
}
