package com.ansh.uimanagement.service;

import static com.ansh.entity.animal.UserProfile.AnimalNotificationSubscriptionStatus.PENDING;

import com.ansh.auth.service.UserProfileService;
import com.ansh.entity.animal.UserProfile;
import com.ansh.entity.subscription.Subscription;
import com.ansh.repository.PendingSubscriberRepository;
import com.ansh.repository.entity.PendingSubscriber;
import com.ansh.utils.IdentifierMasker;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SubscriptionService {

  private static final Logger LOG = LoggerFactory.getLogger(SubscriptionService.class);
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
    if (pendingSubscriberRepository.findByEmailAndTopic(email, topic).isEmpty()) {
      PendingSubscriber newSubscriber = new PendingSubscriber(email, approver, topic);
      pendingSubscriberRepository.save(newSubscriber);
      LOG.debug("[pending subscriber] {} for topic {}",
          IdentifierMasker.maskEmail(newSubscriber.getEmail()),
          newSubscriber.getTopic());
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
    String url = String.format("%s/internal/animal-notify-all-approver-subscriptions",
        animalShelterNotificationApp);
    HttpEntity<String> entity = createHttpEntity(Map.of("approver", approver));

    return restTemplate.exchange(url, HttpMethod.POST, entity,
            new ParameterizedTypeReference<List<Subscription>>() {
            })
        .getBody();
  }

  public UserProfile.AnimalNotificationSubscriptionStatus getStatusByApprover(String approver) {
    String url = String.format("%s/internal/animal-notify-approver-status",
        animalShelterNotificationApp);
    HttpEntity<String> entity = createHttpEntity(Map.of("approver", approver));

    return restTemplate.exchange(url, HttpMethod.POST, entity,
            new ParameterizedTypeReference<UserProfile.AnimalNotificationSubscriptionStatus>() {
            })
        .getBody();
  }

  private HttpEntity<String> createHttpEntity(Map<String, String> requestBody) {
    HttpHeaders headers = new HttpHeaders();
    headers.set("X-API-KEY", notificationApiKey);
    headers.setContentType(MediaType.APPLICATION_JSON);

    try {
      ObjectMapper objectMapper = new ObjectMapper();
      String jsonBody = objectMapper.writeValueAsString(requestBody);
      return new HttpEntity<>(jsonBody, headers);
    } catch (JsonProcessingException e) {
      //TODO fix it
      throw new RuntimeException("Error during JSON creation", e);
    }
  }

  protected void setAnimalShelterNotificationApp(String animalShelterNotificationApp) {
    this.animalShelterNotificationApp = animalShelterNotificationApp;
  }

  protected void setNotificationApiKey(String notificationApiKey) {
    this.notificationApiKey = notificationApiKey;
  }
}
