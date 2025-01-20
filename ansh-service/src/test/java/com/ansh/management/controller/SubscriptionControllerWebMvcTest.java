package com.ansh.management.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ansh.AnshSecurityConfig;
import com.ansh.entity.animal.UserProfile.AnimalNotificationSubscriptionStatus;
import com.ansh.entity.subscription.Subscription;
import com.ansh.repository.entity.PendingSubscriber;
import com.ansh.management.service.AnimalTopicPendingSubscriptionService;
import com.ansh.management.service.NotificationSubscriptionService;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(SubscriptionController.class)
@Import(AnshSecurityConfig.class)
class SubscriptionControllerWebMvcTest extends AbstractControllerWebMvcTest {

  private static final String AUTH_HEADER = HttpHeaders.AUTHORIZATION;
  private static final String APPROVER_EMAIL = "admin@example.com";
  private static final String TEST_EMAIL = "test@example.com";

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private NotificationSubscriptionService notificationSubscriptionService;

  @MockBean
  private AnimalTopicPendingSubscriptionService animalTopicPendingSubscriptionService;

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
    when(animalTopicPendingSubscriptionService.getSubscribersByApprover(eq(APPROVER_EMAIL)))
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
    when(animalTopicPendingSubscriptionService.getPendingSubscribersWithoutApprover())
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
    when(notificationSubscriptionService.getAllSubscriptionByApprover(eq(APPROVER_EMAIL)))
        .thenReturn(List.of(mockSubscription));

    mockMvc.perform(post("/animal-notify-all-approver-subscriptions")
            .header(AUTH_HEADER, BEARER_TOKEN)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"approver\":\"" + APPROVER_EMAIL + "\"}"))
        .andExpect(status().isOk());
  }

  @Test
  void shouldReturnApproverStatus() throws Exception {
    AnimalNotificationSubscriptionStatus mockStatus =
        AnimalNotificationSubscriptionStatus.ACTIVE;
    when(notificationSubscriptionService.getStatusByApprover(eq(APPROVER_EMAIL)))
        .thenReturn(mockStatus);

    mockMvc.perform(post("/animal-notify-approver-status")
            .header(AUTH_HEADER, BEARER_TOKEN)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"approver\":\"" + APPROVER_EMAIL + "\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").value(mockStatus.name()));
  }

  @Test
  void shouldReturnEmptyForInvalidApproverStatusRequest() throws Exception {
    mockMvc.perform(post("/animal-notify-approver-status")
            .header(AUTH_HEADER, BEARER_TOKEN)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{}"))
        .andExpect(status().isOk())
        .andExpect(content().string(""));
  }
}
