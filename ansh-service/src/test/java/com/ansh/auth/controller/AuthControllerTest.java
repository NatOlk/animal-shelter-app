package com.ansh.auth.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.ansh.auth.service.JwtService;
import com.ansh.auth.service.UserProfileService;
import com.ansh.entity.animal.UserProfile;
import jakarta.servlet.http.HttpSession;
import java.util.Map;
import java.util.Optional;
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

class AuthControllerTest {

  @Mock
  private AuthenticationManager authenticationManager;

  @Mock
  private UserProfileService userProfileService;

  @Mock
  private JwtService jwtService;

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

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(identifier);

    UserProfile userProfile = new UserProfile();
    userProfile.setEmail(email);
    userProfile.setName("testUser");

    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenReturn(authentication);
    when(jwtService.generateToken(identifier)).thenReturn(token);
    when(userProfileService.findAuthenticatedUser()).thenReturn(Optional.of(userProfile));

    ResponseEntity<Object> response = authController.login(identifier, password);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
    assertEquals(token, responseBody.get("token"));
    assertEquals(email, responseBody.get("email"));

    verify(authenticationManager, times(1)).authenticate(
        any(UsernamePasswordAuthenticationToken.class));
    verify(jwtService, times(1)).generateToken(identifier);
    verify(userProfileService, times(1)).findAuthenticatedUser();
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
    verifyNoInteractions(jwtService, userProfileService);
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
    verifyNoInteractions(jwtService, userProfileService);
  }

  @Test
  void shouldReturnNotFound_whenUserProfileIsAbsent() {
    String identifier = "testUser";
    String password = "password123";

    Authentication authentication = mock(Authentication.class);

    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenReturn(authentication);
    when(userProfileService.findAuthenticatedUser()).thenReturn(Optional.empty());

    ResponseEntity<Object> response = authController.login(identifier, password);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("User profile not found", response.getBody());

    verify(authenticationManager, times(1)).authenticate(
        any(UsernamePasswordAuthenticationToken.class));
    verify(userProfileService, times(1)).findAuthenticatedUser();
  }

  @Test
  void shouldLogoutSuccessfully() {
    ResponseEntity<String> response = authController.logout(session);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Logout successful", response.getBody());
    verify(session, times(1)).invalidate();
  }
}
