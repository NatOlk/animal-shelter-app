package com.ansh.notification.external;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class ExternalNotificationServiceClientTest {

  private WebClient.Builder webClientBuilder;
  private WebClient webClient;
  private WebClient.RequestBodyUriSpec requestBodyUriSpec;
  private WebClient.RequestBodySpec requestBodySpec;
  private WebClient.RequestHeadersSpec requestHeadersSpec;
  private WebClient.ResponseSpec responseSpec;

  private ExternalNotificationServiceClient client;

  @BeforeEach
  void setUp() {
    webClientBuilder = mock(WebClient.Builder.class);
    webClient = mock(WebClient.class);
    requestBodyUriSpec = mock(WebClient.RequestBodyUriSpec.class);
    requestBodySpec = mock(WebClient.RequestBodySpec.class);
    requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
    responseSpec = mock(WebClient.ResponseSpec.class);

    when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
    when(webClientBuilder.build()).thenReturn(webClient);

    client = new ExternalNotificationServiceClient(webClientBuilder,
        "http://mock-service", "test-key");
  }

  @Test
  void shouldReturnResponseOnSuccessfulPost() {
    String endpoint = "/notify";
    Map<String, Object> requestBody = Map.of("name", "Test");
    Map<String, String> expectedResponse = Map.of("status", "ok");

    when(webClient.post()).thenReturn(requestBodyUriSpec);
    when(requestBodyUriSpec.uri("http://mock-service" + endpoint)).thenReturn(requestBodySpec);
    when(requestBodySpec.headers(any())).thenReturn(requestBodySpec);
    when(requestBodySpec.contentType(MediaType.APPLICATION_JSON)).thenReturn(requestBodySpec);
    when(requestBodySpec.bodyValue(requestBody)).thenReturn(requestHeadersSpec);
    when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
    when(responseSpec.bodyToMono(
        ArgumentMatchers.<ParameterizedTypeReference<Map<String, String>>>any()))
        .thenReturn(Mono.just(expectedResponse));

    Mono<Map<String, String>> result = client.post(endpoint, requestBody,
        new ParameterizedTypeReference<Map<String, String>>() {
        });

    StepVerifier.create(result)
        .expectNext(expectedResponse)
        .verifyComplete();
  }

  @Test
  void shouldReturnErrorMonoOnFailure() {
    String endpoint = "/notify";
    Map<String, Object> requestBody = Map.of("name", "Test");

    when(webClient.post()).thenReturn(requestBodyUriSpec);
    when(requestBodyUriSpec.uri("http://mock-service" + endpoint)).thenReturn(requestBodySpec);
    when(requestBodySpec.headers(any())).thenReturn(requestBodySpec);
    when(requestBodySpec.contentType(MediaType.APPLICATION_JSON)).thenReturn(requestBodySpec);
    when(requestBodySpec.bodyValue(requestBody)).thenReturn(requestHeadersSpec);
    when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

    when(responseSpec.onStatus(any(), any())).thenAnswer(invocation -> {
      ClientResponse mockResponse = mock(ClientResponse.class);
      when(mockResponse.statusCode()).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR);
      when(mockResponse.bodyToMono(String.class)).thenReturn(Mono.just("Internal error"));
      return responseSpec;
    });

    when(responseSpec.bodyToMono(ArgumentMatchers.<ParameterizedTypeReference<Map<String, String>>>any()))
        .thenReturn(Mono.error(new RuntimeException("WebClient request failed with status: 500 INTERNAL_SERVER_ERROR")));

    Mono<Map<String, String>> result = client.post(endpoint, requestBody,
        new ParameterizedTypeReference<Map<String, String>>() {});

    StepVerifier.create(result)
        .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
            throwable.getMessage().contains("500"))
        .verify();
  }

  @Test
  void shouldCallWebClientWithCorrectHeaders() {
    String endpoint = "/notify";
    Map<String, Object> requestBody = Map.of("key", "value");
    Map<String, String> expectedResponse = Map.of("response", "ok");

    when(webClient.post()).thenReturn(requestBodyUriSpec);
    when(requestBodyUriSpec.uri("http://mock-service" + endpoint)).thenReturn(requestBodySpec);
    when(requestBodySpec.headers(any())).then(invocation -> {
      invocation.getArgument(0, java.util.function.Consumer.class)
          .accept(new org.springframework.http.HttpHeaders());
      return requestBodySpec;
    });
    when(requestBodySpec.contentType(MediaType.APPLICATION_JSON)).thenReturn(requestBodySpec);
    when(requestBodySpec.bodyValue(requestBody)).thenReturn(requestHeadersSpec);
    when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
    when(responseSpec.bodyToMono(ArgumentMatchers.<ParameterizedTypeReference<Map<String, String>>>any()))
        .thenReturn(Mono.just(expectedResponse));

    Mono<Map<String, String>> result = client.post(endpoint, requestBody,
        new ParameterizedTypeReference<Map<String, String>>() {});

    StepVerifier.create(result)
        .expectNext(expectedResponse)
        .verifyComplete();
  }
}

