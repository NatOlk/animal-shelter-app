package com.ansh.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Animal Shelter Notification API",
        version = "1.0",
        description = "API for animal shelter notification endpoints"
    )
)
public class AnshNotificationOpenApiConfig {
}
