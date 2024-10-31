package com.ansh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = "com.ansh")
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.ansh")
@EntityScan(basePackages = "com.ansh.entity.subscription")
@Configuration
public class AnimalShelterServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(AnimalShelterServiceApplication.class, args);
  }
}
