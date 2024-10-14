package com.ansh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication(scanBasePackages = "com.ansh")
@Configuration
public class AnimalShelterApp {
	public static void main(String[] args) {
		SpringApplication.run(AnimalShelterApp.class, args);
	}
}
