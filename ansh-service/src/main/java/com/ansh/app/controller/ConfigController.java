package com.ansh.app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "Configuration", description = "Provides static configuration lists for the application")
public class ConfigController {

  private final AppConfig appConfig;

  public ConfigController(AppConfig appConfig) {
    this.appConfig = appConfig;
  }

  @Operation(
      summary = "Get application configuration",
      description = "Returns lists of predefined configuration values like genders, species, colors, etc.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Successfully retrieved config data",
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = AppConfig.class))),
          @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token required")
      }
  )
  @GetMapping("/config")
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
