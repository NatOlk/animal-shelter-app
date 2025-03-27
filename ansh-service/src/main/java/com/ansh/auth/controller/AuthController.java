package com.ansh.auth.controller;

import com.ansh.app.service.user.UserProfileService;
import com.ansh.auth.service.JwtService;
import com.ansh.auth.service.impl.CustomUserDetails;
import com.ansh.dto.RegisterUserRequest;
import com.ansh.entity.account.UserProfile;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/auth")
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
    String maskedIdentifier = IdentifierMasker.maskIdentifier(identifier);
    try {
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(identifier, password));
      LOG.debug("[auth] Authentication successful for user: {}", maskedIdentifier);
      return createAuthenticationResponse(authentication);
    } catch (BadCredentialsException e) {
      return createErrorResponse(HttpStatus.UNAUTHORIZED, "Invalid credentials", maskedIdentifier);
    } catch (Exception e) {
      return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred",
          maskedIdentifier);
    }
  }

  @PostMapping("/logout")
  public ResponseEntity<String> logout(HttpSession session) {
    session.invalidate();
    return ResponseEntity.ok("Logout successful");
  }

  @PostMapping("/register")
  public UserProfile registerUser(@RequestBody RegisterUserRequest request) {
    return userProfileService.registerUser(request.getIdentifier(), request.getEmail(),
        request.getPassword());
  }

  private ResponseEntity<Object> createAuthenticationResponse(Authentication authentication) {
    CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put("token", jwtService.generateToken(authentication));
    responseMap.put("email", user.getEmail());
    responseMap.put("name", user.getName());
    responseMap.put("roles", user.getRoleNames());
    return ResponseEntity.ok(responseMap);
  }

  private ResponseEntity<Object> createErrorResponse(HttpStatus status, String message,
      String maskedIdentifier) {
    LOG.error("[auth] {} for identifier {}", message, maskedIdentifier);
    return ResponseEntity.status(status).body(message);
  }
}
