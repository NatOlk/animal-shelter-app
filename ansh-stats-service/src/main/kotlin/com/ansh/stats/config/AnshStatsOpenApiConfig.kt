package com.ansh.stats.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.context.annotation.Configuration

@Configuration
@OpenAPIDefinition(
    info = Info(
        title = "Animal Shelter Stats API",
        version = "1.0",
        description = "API for accessing statistics about animals, vaccinations, and subscriptions"
    )
)
class OpenApiConfig
