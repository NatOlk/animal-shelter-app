package com.ansh.notification.external;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class ExternalNotificationServiceClient {

  private static final Logger LOG = LoggerFactory.getLogger(
      ExternalNotificationServiceClient.class);
  private static final String CIRCUIT_BREAKER_NAME = "notificationService";
  private static final String RETRY_NAME = "notificationRetry";

  private final WebClient webClient;
  private final String notificationApiKey;
  private final String animalShelterNotificationApp;

  public ExternalNotificationServiceClient(
      WebClient.Builder webClientBuilder,
      @Value("${app.animalShelterNotificationApp}") String animalShelterNotificationApp,
      @Value("${notification.api.key}") String notificationApiKey
  ) {
    this.animalShelterNotificationApp = animalShelterNotificationApp;
    this.notificationApiKey = notificationApiKey;
    this.webClient = webClientBuilder.baseUrl(animalShelterNotificationApp).build();
  }

  @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "fallback")
  @Retry(name = RETRY_NAME, fallbackMethod = "fallback")
  public <T> Mono<T> post(String endpoint, Map<String, Object> requestBody,
      ParameterizedTypeReference<T> responseType) {
    return webClient.post()
        .uri(animalShelterNotificationApp + endpoint)
        .headers(headers -> headers.set("X-API-KEY", notificationApiKey))
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(requestBody)
        .retrieve()
        .onStatus(HttpStatusCode::isError, response ->
            response.bodyToMono(String.class)
                .flatMap(errorBody -> {
                  LOG.error("WebClient error: {} - {}", response.statusCode(), errorBody);
                  return Mono.error(new RuntimeException(
                      "WebClient request failed with status: " + response.statusCode()));
                })
        )
        .bodyToMono(responseType);
  }
}
