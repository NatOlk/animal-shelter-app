package com.ansh.auth.service;

import com.ansh.auth.service.impl.CustomUserDetails;
import com.ansh.entity.account.UserProfile;
import com.ansh.entity.account.UserProfile.Role;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private static final Logger LOG = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

  private final JwtService jwtService;
  private final List<String> publicPaths;

  public JwtAuthenticationFilter(JwtService jwtService,
      @Value("${security.public-paths}") String publicPaths) {
    this.jwtService = jwtService;
    this.publicPaths = Arrays.asList(publicPaths.split(","));
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    var requestURI = request.getRequestURI();
    var shouldNotFiltered = publicPaths.stream().anyMatch(requestURI::startsWith);
    LOG.debug("Filter path {} ? {}", requestURI, !shouldNotFiltered);
    return shouldNotFiltered;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws IOException {
    final String authHeader = request.getHeader("Authorization");

    LOG.debug("Checking jwt token for path {} ", request.getRequestURI());
    if (authHeader == null || !authHeader.startsWith("Bearer ") || authHeader.length() <= 7) {
      LOG.debug("Alert: no authorization token or token is too short!");
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.getWriter().write("Unauthorized: Missing or invalid Authorization header");
      return;
    }
    final String jwt = authHeader.substring(7);

    try {
      final String username = jwtService.extractUsername(jwt);
      if (username != null && SecurityContextHolder.getContext().getAuthentication() == null
          && jwtService.isTokenValid(jwt)) {
        final String email = jwtService.extractEmail(jwt);
        final String name = jwtService.extractName(jwt);
        final Long id = jwtService.extractId(jwt);
        final List<String> roles = jwtService.extractRoles(jwt);
        var authorities = roles.stream()
            .map(role -> new SimpleGrantedAuthority(STR."ROLE_\{role}"))
            .collect(Collectors.toList());

        UserProfile profile = new UserProfile();
        profile.setId(id);
        profile.setName(name);
        profile.setEmail(email);
        profile.setRoles(
            roles.stream()
                .map(String::trim)
                .map(UserProfile.Role::valueOf)
                .collect(Collectors.toSet())
        );

        CustomUserDetails userDetails = new CustomUserDetails(profile);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            userDetails, null, authorities);
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authToken);
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
