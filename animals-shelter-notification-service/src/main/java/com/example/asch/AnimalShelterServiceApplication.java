package com.example.ansh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication(scanBasePackages = "com.example.gr")
@Configuration
public class AnimalShelterServiceApplication
{
	public static void main(String[] args)
	{
		SpringApplication.run(AnimalShelterServiceApplication.class, args);
	}
}
