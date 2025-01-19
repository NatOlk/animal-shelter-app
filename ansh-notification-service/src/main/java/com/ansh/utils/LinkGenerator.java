package com.ansh.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LinkGenerator {

  @Value("${animalShelterNotificationApp}")
  private String animalShelterNotificationApp;

  @Value("${unsubscribeEndpoint}")
  private String unsubscribeEndpoint;

  @Value("${confirmationEndpoint}")
  private String confirmationEndpoint;

  public String generateUnsubscribeLink(String token) {
    return animalShelterNotificationApp + unsubscribeEndpoint + token;
  }

  public String generateConfirmationLink(String token) {
    return animalShelterNotificationApp + confirmationEndpoint + token;
  }
}
