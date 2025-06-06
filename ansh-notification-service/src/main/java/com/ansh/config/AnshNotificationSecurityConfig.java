package com.ansh.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class AnshNotificationSecurityConfig {

  @Autowired
  private ApiKeyAuthFilter apiKeyAuthFilter;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.ignoringRequestMatchers(
            "/external/**", "/internal/**"
        ))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/**").permitAll()
            .requestMatchers("/external/**").permitAll()
            .requestMatchers(
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/swagger-ui.html"
            ).permitAll()
            .requestMatchers("/internal/**").authenticated()
        )
        .addFilterBefore(apiKeyAuthFilter, BasicAuthenticationFilter.class);

    return http.build();
  }
}
