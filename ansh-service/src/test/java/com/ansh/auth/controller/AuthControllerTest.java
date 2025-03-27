package com.ansh.auth.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.ansh.auth.service.impl.JwtServiceImpl;
import com.ansh.entity.account.UserProfile;
import com.ansh.auth.service.impl.CustomUserDetails;
import com.ansh.entity.account.UserProfile.Role;
import jakarta.servlet.http.HttpSession;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Map;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

class AuthControllerTest {

  @Mock
  private AuthenticationManager authenticationManager;

  @Mock
  private JwtServiceImpl jwtService;

  @InjectMocks
  private AuthController authController;

  @Mock
  private HttpSession session;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void shouldLoginSuccessfully() {
    String identifier = "testUser";
    String password = "password123";
    String token = "jwtToken";
    String email = "test@example.com";

    UserProfile userProfile = new UserProfile();
    userProfile.setEmail(email);
    userProfile.setName(identifier);
    userProfile.setRoles(Set.of(Role.ADMIN));

    CustomUserDetails userDetails = new CustomUserDetails(userProfile);

    Authentication authentication = mock(Authentication.class);
    when(authentication.getPrincipal()).thenReturn(userDetails);
    when(authentication.getName()).thenReturn(identifier);

    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenReturn(authentication);

    when(jwtService.generateToken(authentication)).thenReturn(token);

    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
    securityContext.setAuthentication(authentication);
    SecurityContextHolder.setContext(securityContext);

    ResponseEntity<Object> response = authController.login(identifier, password);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
    assertEquals(token, responseBody.get("token"));
    assertEquals(identifier, responseBody.get("name"));

    verify(authenticationManager, times(1)).authenticate(
        any(UsernamePasswordAuthenticationToken.class));
    verify(jwtService, times(1)).generateToken(authentication);
  }

  @Test
  void shouldReturnUnauthorized_whenInvalidCredentials() {
    String identifier = "invalidUser";
    String password = "wrongPassword";

    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenThrow(new BadCredentialsException("Invalid credentials"));

    ResponseEntity<Object> response = authController.login(identifier, password);

    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    assertEquals("Invalid credentials", response.getBody());

    verify(authenticationManager, times(1)).authenticate(
        any(UsernamePasswordAuthenticationToken.class));
    verifyNoInteractions(jwtService);
  }

  @Test
  void shouldReturnInternalServerError_whenException() {
    String identifier = "invalidUser";
    String password = "wrongPassword";

    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenThrow(new RuntimeException());

    ResponseEntity<Object> response = authController.login(identifier, password);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertEquals("An unexpected error occurred", response.getBody());

    verify(authenticationManager, times(1)).authenticate(
        any(UsernamePasswordAuthenticationToken.class));
    verifyNoInteractions(jwtService);
  }

  @Test
  void shouldLogoutSuccessfully() {
    ResponseEntity<String> response = authController.logout(session);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Logout successful", response.getBody());
    verify(session, times(1)).invalidate();
  }
}
