package com.ansh.app.controller;

import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfigController {

  private final AppConfig appConfig;

  public ConfigController(AppConfig appConfig) {
    this.appConfig = appConfig;
  }

  @GetMapping("/api/config")
  public AppConfig getConfig() {
    return appConfig;
  }

  @Data
  @Component
  @ConfigurationProperties(prefix = "app")
  public static class AppConfig {
    private List<String> genders;
    private List<String> species;
    private List<String> colors;
    private List<String> vaccines;
    private String animalShelterNotificationApp;
    private String animalShelterApp;
  }
}
