package com.ansh.notification.external;

import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class ExternalNotificationServiceClient {

  private final String notificationApiKey;
  private final String animalShelterNotificationApp;
  private final WebClient webClient;

  public ExternalNotificationServiceClient(
      WebClient.Builder webClientBuilder,
      @Value("${animalShelterNotificationApp}") String animalShelterNotificationApp,
      @Value("${notification.api.key}") String notificationApiKey) {
    this.notificationApiKey = notificationApiKey;
    this.animalShelterNotificationApp = animalShelterNotificationApp;
    this.webClient = webClientBuilder.baseUrl(animalShelterNotificationApp).build();
  }

  public <T> T post(String endpoint, Map<String, Object> requestBody, Class<T> responseType) {
    return webClient.post()
        .uri(animalShelterNotificationApp + endpoint)
        .headers(headers -> headers.set("X-API-KEY", notificationApiKey))
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(requestBody)
        .retrieve()
        .bodyToMono(responseType)
        .block();
  }
}
