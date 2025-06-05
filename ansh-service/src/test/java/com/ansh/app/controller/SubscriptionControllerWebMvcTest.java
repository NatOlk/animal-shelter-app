package com.ansh.app.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ansh.config.AnshSecurityConfig;
import com.ansh.app.facade.SubscriptionFacade;
import com.ansh.dto.NotificationStatusDTO;
import com.ansh.entity.account.UserProfile.AnimalInfoNotifStatus;
import com.ansh.entity.subscription.Subscription;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.request.async.DeferredResult;

@WebMvcTest({SubscriptionController.class})
@Import(AnshSecurityConfig.class)
class SubscriptionControllerWebMvcTest extends AbstractControllerWebMvcTest {

  private static final String AUTH_HEADER = HttpHeaders.AUTHORIZATION;
  private static final String APPROVER_EMAIL = "admin@example.com";
  private static final String TEST_EMAIL = "test@example.com";
  private static final String TOPIC_ID = "animalTopicId";

  @MockBean
  private SubscriptionFacade subscriptionFacade;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @WithMockUser(username = "test@example.com", roles = {"ADMIN"})
  void shouldReturnSubscribers() throws Exception {
    List<Subscription> subscriptions = List.of(new Subscription());
    DeferredResult<List<Subscription>> deferredResult = new DeferredResult<>();
    ForkJoinPool.commonPool().submit(() -> deferredResult.setResult(subscriptions));

    when(subscriptionFacade.getAllSubscribers(any())).thenReturn(deferredResult);

    MvcResult mvcResult = mockMvc.perform(post("/api/subscription/all")
            .header(AUTH_HEADER, BEARER_TOKEN)
            .contentType(MediaType.APPLICATION_JSON)
            .content(STR."{\"approver\":\"\{APPROVER_EMAIL}\"}"))
        .andReturn();

    assertEquals(subscriptions, mvcResult.getAsyncResult());
  }

  @Test
  @WithMockUser(username = "test@example.com", roles = {"ADMIN"})
  void shouldReturnApproverStatus() throws Exception {
    NotificationStatusDTO expectedStatus = new NotificationStatusDTO(
        AnimalInfoNotifStatus.ACTIVE,
        AnimalInfoNotifStatus.NONE,
        AnimalInfoNotifStatus.UNKNOWN
    );
    DeferredResult<NotificationStatusDTO> deferredResult = new DeferredResult<>();
    ForkJoinPool.commonPool().submit(() -> deferredResult.setResult(expectedStatus));

    when(subscriptionFacade.getNotificationStatusesByAccount(any())).thenReturn(deferredResult);

    MvcResult mvcResult = mockMvc.perform(post("/api/subscription/statuses")
            .header(AUTH_HEADER, BEARER_TOKEN)
            .contentType(MediaType.APPLICATION_JSON)
            .content(STR."{\"approver\":\"\{APPROVER_EMAIL}\"}"))
        .andReturn();

    assertEquals(expectedStatus, mvcResult.getAsyncResult());
  }

  @Test
  @WithMockUser(username = "test@example.com", roles = {"ADMIN"})
  void shouldReturnBadRequestForInvalidRequest() throws Exception {
    mockMvc.perform(post("/api/subscription/statuses")
            .header(AUTH_HEADER, BEARER_TOKEN)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{invalid-json}"))
        .andExpect(status().is5xxServerError());
  }

  @Test
  @WithMockUser(username = "test@example.com", roles = {"ADMIN"})
  void shouldReturnUnauthorizedWhenNoAuthHeader() throws Exception {
    mockMvc.perform(post("/api/subscription/statuses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(STR."{\"approver\":\"\{APPROVER_EMAIL}\"}"))
        .andExpect(status().isUnauthorized());
  }
}