package com.ansh.auth.controller;

import com.ansh.auth.service.JwtService;
import com.ansh.auth.service.UserProfileService;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);

  @Autowired
  private AuthenticationManager authenticationManager;
  @Autowired
  private UserProfileService userProfileService;
  @Autowired
  private JwtService jwtService;

  @PostMapping("/login")
  public ResponseEntity<Object> login(@RequestParam String identifier,
      @RequestParam String password) {
    String sfId = generateSafeIdentifier(identifier);
    try {
      Authentication authentication = authenticateUser(identifier, password);
      return processAuthenticatedUser(authentication, sfId);
    } catch (BadCredentialsException e) {
      return createErrorResponse(HttpStatus.UNAUTHORIZED, "Invalid credentials", sfId);
    } catch (Exception e) {
      return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred",
          sfId);
    }
  }

  @PostMapping("/logout")
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
      String sfId) {
    LOG.debug("Authentication successful for user: {}", sfId);
    SecurityContextHolder.getContext().setAuthentication(authentication);
    return userProfileService.findAuthenticatedUser()
        .map(
            userProfile -> createAuthenticationResponse(userProfile.getEmail(), authentication,
                sfId))
        .orElseGet(() -> createErrorResponse(HttpStatus.NOT_FOUND, "User profile not found", sfId));
  }

  private ResponseEntity<Object> createAuthenticationResponse(String email,
      Authentication authentication,
      String sfId) {
    return extractMailParts(email)
        .map(emailParts -> {
          Map<String, Object> responseMap = new HashMap<>();
          responseMap.put("token", jwtService.generateToken(authentication.getName()));
          responseMap.put("email", email);
          LOG.debug("Token generated for user: {}***@.{}", emailParts.localPart(),
              emailParts.domain());
          return ResponseEntity.<Object>ok(responseMap);
        })
        .orElseGet(() -> createErrorResponse(HttpStatus.NOT_FOUND, "Invalid email format", sfId));
  }

  private String generateSafeIdentifier(String identifier) {
    return extractSafeIdentifier(identifier).map(group -> group + "***").orElse("Unknown");
  }

  private Optional<String> extractSafeIdentifier(String input) {
    Matcher matcher = ValidationPatterns.IDENTIFIER_PATTERN.matcher(input);
    return matcher.find() ? Optional.of(matcher.group(1)) : Optional.empty();
  }

  private Optional<EmailParts> extractMailParts(String email) {
    Matcher matcher = ValidationPatterns.EMAIL_PATTERN.matcher(email);
    if (matcher.find()) {
      return Optional.of(new EmailParts(matcher.group(1), matcher.group(2)));
    }
    return Optional.empty();
  }

  private ResponseEntity<Object> createErrorResponse(HttpStatus status, String message,
      String sfId) {
    LOG.error("{} for identifier {}", message, sfId);
    return ResponseEntity.status(status).body(message);
  }

  public record EmailParts(String localPart, String domain) {

  }

  private static class ValidationPatterns {

    public static final Pattern EMAIL_PATTERN = Pattern.compile("^([a-zA-Z]{2}).*\\.([a-z]+)$");
    public static final Pattern IDENTIFIER_PATTERN = Pattern.compile("^([a-zA-Z0-9]{2}).*");
  }
}
