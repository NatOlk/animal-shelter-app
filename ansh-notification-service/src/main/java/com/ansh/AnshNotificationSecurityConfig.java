package com.ansh;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@SuppressWarnings("squid:S4502") //use jwt tokens
public class AnshNotificationSecurityConfig {

  @Autowired
  private ApiKeyAuthFilter apiKeyAuthFilter;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http

        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/**").permitAll()
            .requestMatchers("/external/**").permitAll()
            .requestMatchers("/internal/**").authenticated()
        )
        .addFilterBefore(apiKeyAuthFilter, BasicAuthenticationFilter.class);

    return http.build();
  }
}
