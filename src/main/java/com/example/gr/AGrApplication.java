package com.example.gr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication(scanBasePackages = "com.example.gr")
@Configuration
public class AGrApplication
{

	public static void main(String[] args)
	{
		SpringApplication.run(AGrApplication.class, args);
	}
}
