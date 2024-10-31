package com.ansh.uimanagement.controller;

import com.ansh.entity.subscription.Subscription;
import com.ansh.repository.entity.PendingSubscriber;
import com.ansh.uimanagement.service.SubscriptionService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class SubscriptionController {

  @Value("${animalShelterNotificationApp}")
  private String animalShelterNotificationApp;
  @Value("${notification.api.key}")
  private String notificationApiKey;

  @Autowired
  private SubscriptionService subscriptionService;

  @Autowired
  private RestTemplate restTemplate;

  @PostMapping("/animal-notify-approve")
  public void approve(@RequestBody String email) {
    //TODO: fix it
    email = email.replace("\"", "");
    subscriptionService.approveSubscriber(email);
  }

  @GetMapping("/pending-subscribers")
  public List<PendingSubscriber> getPendingSubscribers() {
    return subscriptionService.getPendingSubscribers();
  }

  @GetMapping("/subscribers")
  public List<Subscription> getSubscribers() {
    String url = animalShelterNotificationApp + "/internal/animal-notify-all-subscribes";

    HttpHeaders headers = new HttpHeaders();
    headers.set("X-API-KEY", notificationApiKey);

    HttpEntity<String> entity = new HttpEntity<>(headers);

    ResponseEntity<List<Subscription>> response = restTemplate.exchange(
        url,
        HttpMethod.GET,
        entity,
        new ParameterizedTypeReference<>() {
        }
    );

    return response.getBody();
  }
}
