package com.ansh.uimanagement.service;

import static com.ansh.entity.animal.UserProfile.AnimalNotificationSubscriptionStatus.PENDING;

import com.ansh.auth.service.UserProfileService;
import com.ansh.entity.animal.UserProfile;
import com.ansh.entity.subscription.Subscription;
import com.ansh.repository.PendingSubscriberRepository;
import com.ansh.repository.entity.PendingSubscriber;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SubscriptionService {

  @Value("${animalShelterNotificationApp}")
  private String animalShelterNotificationApp;
  @Value("${notification.api.key}")
  private String notificationApiKey;
  @Autowired
  private RestTemplate restTemplate;
  @Autowired
  private PendingSubscriberRepository pendingSubscriberRepository;
  @Autowired
  private UserProfileService userProfileService;

  @Transactional
  public void savePendingSubscriber(String email, String approver, String topic) {
    Optional<PendingSubscriber> pendingSubscriber =
        pendingSubscriberRepository.findByEmailAndTopic(email, topic);
    if (pendingSubscriber.isEmpty()) {
      PendingSubscriber newSubscriber = new PendingSubscriber();
      newSubscriber.setEmail(email);
      newSubscriber.setApprover(approver);
      newSubscriber.setTopic(topic);
      pendingSubscriberRepository.save(newSubscriber);

      userProfileService.updateAnimalNotificationSubscriptionStatus(email, PENDING);
    }
  }

  public List<PendingSubscriber> getPendingSubscribers(String approver) {
    return pendingSubscriberRepository.findByApprover(approver);
  }

  public List<PendingSubscriber> getPendingNoApproverSubscribers() {
    return pendingSubscriberRepository.findByApproverIsNullOrEmpty();
  }

  public List<Subscription> getAllSubscriptionByApprover(String approver) {
    String url = animalShelterNotificationApp + "/internal/animal-notify-all-approver-subscriptions";

    HttpHeaders headers = new HttpHeaders();
    headers.set("X-API-KEY", notificationApiKey);
    headers.setContentType(MediaType.APPLICATION_JSON);

    Map<String, String> requestBody = new HashMap<>();
    requestBody.put("approver", approver);

    ObjectMapper objectMapper = new ObjectMapper();
    String jsonBody;
    try {
      jsonBody = objectMapper.writeValueAsString(requestBody);
    } catch (Exception e) {
      throw new RuntimeException("Error during json creation ", e);
    }

    HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

    ResponseEntity<List<Subscription>> response = restTemplate.exchange(url, HttpMethod.POST,
        entity, new ParameterizedTypeReference<>() {});

    return response.getBody();
  }

  public UserProfile.AnimalNotificationSubscriptionStatus getStatusByApprover(String approver) {
    String url = animalShelterNotificationApp + "/internal/animal-notify-approver-status";

    HttpHeaders headers = new HttpHeaders();
    headers.set("X-API-KEY", notificationApiKey);
    headers.setContentType(MediaType.APPLICATION_JSON);

    Map<String, String> requestBody = new HashMap<>();
    requestBody.put("approver", approver);

    ObjectMapper objectMapper = new ObjectMapper();
    String jsonBody;
    try {
      jsonBody = objectMapper.writeValueAsString(requestBody);
    } catch (Exception e) {
      throw new RuntimeException("Error during json creation ", e);
    }

    HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

    ResponseEntity<UserProfile.AnimalNotificationSubscriptionStatus> response = restTemplate.exchange(url, HttpMethod.POST,
        entity, new ParameterizedTypeReference<>() {});

    return response.getBody();
  }
}
