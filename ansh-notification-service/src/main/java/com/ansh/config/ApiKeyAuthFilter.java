package com.ansh.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class ApiKeyAuthFilter extends OncePerRequestFilter {

  @Value("${notification.api.key}")
  private String notificationApiKey;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String requestApiKey = request.getHeader("X-API-KEY");

    if (request.getRequestURI().startsWith("/internal/") && !notificationApiKey.equals(requestApiKey)) {
      response.sendError(HttpStatus.UNAUTHORIZED.value(), "Unauthorized");
      return;
    }

    filterChain.doFilter(request, response);
  }
}