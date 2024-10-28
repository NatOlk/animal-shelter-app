package com.ansh.auth.controller;

import com.ansh.auth.service.UserProfileService;
import com.ansh.entity.UserProfile;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private UserProfileService userProfileService;

  @PostMapping("/login")
  public ResponseEntity<Object> login(@RequestParam String identifier,
      @RequestParam String password,
      HttpServletRequest request) {
    try {
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(identifier, password)
      );

      SecurityContextHolder.getContext().setAuthentication(authentication);
      request.getSession()
          .setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
              SecurityContextHolder.getContext());

      Map<String, Object> responseMap = new HashMap<>();
      responseMap.put("user", identifier);

      UserProfile userProfile = userProfileService.findAuthenticatedUser();

      responseMap.put("email", userProfile.getEmail());
      return ResponseEntity.ok(responseMap);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }
  }

  @PostMapping("/logout")
  public ResponseEntity<String> logout(HttpSession session) {
    session.removeAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
    session.invalidate();
    return ResponseEntity.ok("Logout successful");
  }
}