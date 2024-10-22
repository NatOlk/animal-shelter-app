package com.ansh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication(scanBasePackages = "com.ansh")
@Configuration
public class AnimalShelterServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(AnimalShelterServiceApplication.class, args);
  }
}
