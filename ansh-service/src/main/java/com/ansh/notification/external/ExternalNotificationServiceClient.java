package com.ansh.notification.external;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.annotation.PostConstruct;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

  @CircuitBreaker(name = "notificationService", fallbackMethod = "fallback")
  @Retry(name = "notificationRetry", fallbackMethod = "fallback")
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
                      STR."WebClient request failed with status: \{response.statusCode()}"));
                })
        )
        .bodyToMono(responseType);
  }
}
