package com.ansh.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BaseUrlProvider {
  @Value("${app.animalShelterApp}")
  String animalShelterApp;

  public String getBaseUrl() {
    return animalShelterApp;
  }
}
