package com.ansh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = "com.ansh")
@Configuration
@EnableTransactionManagement
@EntityScan(basePackages = "com.ansh.entity.animal, com.ansh.repository.entity, com.ansh.entity.account")
@EnableCaching
public class AnimalShelterApp {

  public static void main(String[] args) {
    SpringApplication.run(AnimalShelterApp.class, args);
  }
}
