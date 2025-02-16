package com.ansh.auth.controller;

import com.ansh.app.service.user.UserProfileService;
import com.ansh.auth.service.JwtService;
import com.ansh.utils.IdentifierMasker;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

  private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);

  @Autowired
  private AuthenticationManager authenticationManager;
  @Autowired
  private UserProfileService userProfileService;
  @Autowired
  private JwtService jwtService;

  @PostMapping("/public/auth/login")
  public ResponseEntity<Object> login(@RequestParam String identifier,
      @RequestParam String password) {
    String maskedIdentifier = IdentifierMasker.maskIdentifier(identifier);
    try {
      Authentication authentication = authenticateUser(identifier, password);
      return processAuthenticatedUser(authentication, maskedIdentifier);
    } catch (BadCredentialsException e) {
      return createErrorResponse(HttpStatus.UNAUTHORIZED, "Invalid credentials", maskedIdentifier);
    } catch (Exception e) {
      return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred",
          maskedIdentifier);
    }
  }

  @PostMapping("/public/auth/logout")
  public ResponseEntity<String> logout(HttpSession session) {
    session.invalidate();
    return ResponseEntity.ok("Logout successful");
  }

  private Authentication authenticateUser(String identifier, String password) {
    return authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(identifier, password)
    );
  }

  private ResponseEntity<Object> processAuthenticatedUser(Authentication authentication,
      String maskedIdentifier) {
    LOG.debug("[auth] Authentication successful for user: {}", maskedIdentifier);
    SecurityContextHolder.getContext().setAuthentication(authentication);
    //TODO why we need to get auth user?
    return userProfileService.getAuthUser()
        .map(userProfile -> createAuthenticationResponse(userProfile.getEmail(), authentication,
            maskedIdentifier))
        .orElseGet(() -> createErrorResponse(HttpStatus.NOT_FOUND, "User profile not found",
            maskedIdentifier));
  }

  private ResponseEntity<Object> createAuthenticationResponse(String email,
      Authentication authentication, String maskedIdentifier) {
    String maskedEmail = IdentifierMasker.maskEmail(email);
    if (!maskedEmail.isEmpty()) {
      Map<String, Object> responseMap = new HashMap<>();
      responseMap.put("token", jwtService.generateToken(authentication.getName()));
      responseMap.put("email", email);
      LOG.debug("[auth] Token generated for user: {}", maskedEmail);
      return ResponseEntity.ok(responseMap);
    } else {
      return createErrorResponse(HttpStatus.NOT_FOUND, "Invalid email format", maskedIdentifier);
    }
  }

  private ResponseEntity<Object> createErrorResponse(HttpStatus status, String message,
      String maskedIdentifier) {
    LOG.error("[auth] {} for identifier {}", message, maskedIdentifier);
    return ResponseEntity.status(status).body(message);
  }
}
