package com.ansh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.SpringSecurityCoreVersion;

@SpringBootApplication(scanBasePackages = "com.ansh")
@Configuration
public class AnimalShelterApp {

  public static void main(String[] args) {
    System.out.println("Spring Security version: " + SpringSecurityCoreVersion.getVersion());
    SpringApplication.run(AnimalShelterApp.class, args);
  }
}
