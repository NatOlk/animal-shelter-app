package com.ansh.app.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ansh.AnshSecurityConfig;
import com.ansh.app.facade.PendingSubscriptionFacade;
import com.ansh.repository.entity.PendingSubscriber;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

@WebMvcTest({PendingSubscriptionController.class})
@Import(AnshSecurityConfig.class)
class PendingSubscriptionControllerWebMvcTest extends AbstractControllerWebMvcTest {

  private static final String AUTH_HEADER = HttpHeaders.AUTHORIZATION;
  private static final String APPROVER_EMAIL = "admin@example.com";
  private static final String TEST_EMAIL = "test@example.com";
  private static final String TOPIC_ID = "animalTopicId";

  @MockBean
  private PendingSubscriptionFacade pendingSubscriptionFacade;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @WithMockUser(username = "test@example.com", roles = {"ADMIN"})
  void shouldApproveSubscriber() throws Exception {
    mockMvc.perform(post("/api/subscription/pending/approve")
            .header(AUTH_HEADER, BEARER_TOKEN)
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                STR."{\"email\":\"\{TEST_EMAIL}\",\"approver\":\"\{APPROVER_EMAIL}\",\"topic\":\"\{TOPIC_ID}\"}"))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "test@example.com", roles = {"ADMIN"})
  void shouldRejectSubscriber() throws Exception {
    mockMvc.perform(post("/api/subscription/pending/reject")
            .header(AUTH_HEADER, BEARER_TOKEN)
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                STR."{\"email\":\"\{TEST_EMAIL}\",\"approver\":\"\{APPROVER_EMAIL}\",\"topic\":\"\{TOPIC_ID}\"}"))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "test@example.com", roles = {"ADMIN"})
  void shouldReturnPendingSubscribers() throws Exception {
    PendingSubscriber pendingSubscriber = PendingSubscriber.builder()
        .email(TEST_EMAIL)
        .approver(APPROVER_EMAIL)
        .topic(TOPIC_ID)
        .build();

    when(pendingSubscriptionFacade.getSubscribersByApprover(APPROVER_EMAIL))
        .thenReturn(List.of(pendingSubscriber));

    mockMvc.perform(post("/api/subscription/pending/subscribers")
            .header(AUTH_HEADER, BEARER_TOKEN)
            .contentType(MediaType.APPLICATION_JSON)
            .content(STR."{\"approver\":\"\{APPROVER_EMAIL}\",\"topic\":\"\{TOPIC_ID}\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].email").value(TEST_EMAIL));
  }

  @Test
  @WithMockUser(username = "test@example.com", roles = {"ADMIN"})
  void shouldReturnPendingNoApproverSubscribers() throws Exception {
    PendingSubscriber pendingSubscriber = PendingSubscriber.builder()
        .email(TEST_EMAIL)
        .approver(APPROVER_EMAIL)
        .topic(TOPIC_ID)
        .build();
    when(pendingSubscriptionFacade.getPendingSubscribersWithoutApprover())
        .thenReturn(List.of(pendingSubscriber));

    mockMvc.perform(get("/api/subscription/pending/no-approver-subscribers")
            .header(AUTH_HEADER, BEARER_TOKEN))
        .andExpect(status().isOk());
  }
}