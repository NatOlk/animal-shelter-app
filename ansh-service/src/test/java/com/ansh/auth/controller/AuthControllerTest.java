package com.ansh.auth.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.ansh.app.service.exception.user.UserAlreadyExistException;
import com.ansh.app.service.user.UserProfileService;
import com.ansh.auth.service.JwtService;
import com.ansh.auth.service.impl.CustomUserDetails;
import com.ansh.dto.RegisterUserRequest;
import com.ansh.entity.account.UserProfile;
import com.ansh.entity.account.UserProfile.Role;
import jakarta.servlet.http.HttpSession;
import java.util.Map;
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
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

class AuthControllerTest {

  @Mock
  private AuthenticationManager authenticationManager;

  @Mock
  private JwtService jwtService;

  @InjectMocks
  private AuthController authController;

  @Mock
  private HttpSession session;

  @Mock
  private UserProfileService userProfileService;

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

  @Test
  void shouldRegisterUserSuccessfully() throws UserAlreadyExistException {
    String identifier = "newuser";
    String email = "newuser@example.com";
    String password = "securepassword";

    RegisterUserRequest request = new RegisterUserRequest();
    request.setIdentifier(identifier);
    request.setEmail(email);
    request.setPassword(password);

    UserProfile createdUser = new UserProfile();
    createdUser.setName(identifier);
    createdUser.setEmail(email);

    when(userProfileService.registerUser(identifier, email, password)).thenReturn(createdUser);

    UserProfile response = authController.registerUser(request);

    assertEquals(identifier, response.getName());
    assertEquals(email, response.getEmail());

    verify(userProfileService, times(1)).registerUser(identifier, email, password);
  }

  @Test
  void shouldThrowUserAlreadyExistException() throws UserAlreadyExistException {
    String identifier = "existinguser";
    String email = "existing@example.com";
    String password = "somepassword";

    RegisterUserRequest request = new RegisterUserRequest();
    request.setIdentifier(identifier);
    request.setEmail(email);
    request.setPassword(password);

    when(userProfileService.registerUser(identifier, email, password))
        .thenThrow(new UserAlreadyExistException("User already exists"));

    try {
      authController.registerUser(request);
    } catch (UserAlreadyExistException ex) {
      assertEquals("User already exists", ex.getMessage());
    }

    verify(userProfileService, times(1)).registerUser(identifier, email, password);
  }
}
