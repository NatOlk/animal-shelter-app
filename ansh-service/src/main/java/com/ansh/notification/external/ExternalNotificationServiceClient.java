package com.ansh.notification.external;

import jakarta.annotation.PostConstruct;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class ExternalNotificationServiceClient {

  @Autowired
  private WebClient.Builder webClientBuilder;

  @Value("${app.animalShelterNotificationApp}")
  private String animalShelterNotificationApp;

  @Value("${notification.api.key}")
  private String notificationApiKey;

  private WebClient webClient;

  @PostConstruct
  public void init() {
    webClient = webClientBuilder.baseUrl(animalShelterNotificationApp).build();
  }

  public <T> Mono<T> post(String endpoint, Map<String, Object> requestBody,
      ParameterizedTypeReference<T> responseType) {
    return webClient.post()
        .uri(animalShelterNotificationApp + endpoint)
        .headers(headers -> headers.set("X-API-KEY", notificationApiKey))
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(requestBody)
        .retrieve()
        .bodyToMono(responseType);
  }
}
