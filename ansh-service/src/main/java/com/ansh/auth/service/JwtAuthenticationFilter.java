package com.ansh.auth.service;

import com.ansh.auth.service.impl.CustomUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private static final Logger LOG = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
  private final JwtService jwtService;
  private final CustomUserDetailsService userDetailsService;
  private final List<String> publicPaths;

  public JwtAuthenticationFilter(
      JwtService jwtService,
      CustomUserDetailsService userDetailsService,
      @Value("${security.public-paths}") String publicPaths) {
    this.jwtService = jwtService;
    this.userDetailsService = userDetailsService;
    this.publicPaths = Arrays.asList(publicPaths.split(","));
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String requestURI = request.getRequestURI();
    boolean shouldNotFiltered = publicPaths.stream().anyMatch(requestURI::startsWith);
    LOG.debug("Should filter path {} ? {}", requestURI, !shouldNotFiltered);
    return shouldNotFiltered;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    final String authHeader = request.getHeader("Authorization");

    LOG.debug("Trying to check jwt token for path {} ", request.getRequestURI());
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      LOG.debug("Alert: no authorization token and bearer!");
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.getWriter().write("Unauthorized: Missing or invalid Authorization header");
      return;
    }
    final String jwt = authHeader.substring(7);

    try {
      final String user = jwtService.extractUsername(jwt);
      if (user != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        var userDetails = userDetailsService.loadUserByUsername(user);
        if (jwtService.isTokenValid(jwt, userDetails)) {
          var authToken = new UsernamePasswordAuthenticationToken(
              userDetails, null, userDetails.getAuthorities());
          authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

          SecurityContextHolder.getContext().setAuthentication(authToken);
        }
      }
      filterChain.doFilter(request, response);

    } catch (ExpiredJwtException e) {
      LOG.error("Token expired!");
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.setHeader("Location", "/login");
      response.flushBuffer();
    } catch (Exception e) {
      LOG.error("Something is wrong with token: {}", e.getMessage());
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.getWriter().write("Unauthorized: Invalid token");
      response.flushBuffer();
    }
  }
}
