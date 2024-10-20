package com.ansh.auth.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
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

  @PostMapping("/login")
  public ResponseEntity<Object> login(@RequestParam String identifier, @RequestParam String password,
      HttpServletRequest request,
      HttpServletResponse response) {
    try {
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(identifier, password)
      );

      SecurityContextHolder.getContext().setAuthentication(authentication);
      request.getSession()
          .setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
              SecurityContextHolder.getContext());

      String sessionId = request.getSession().getId();

      Cookie cookie = new Cookie("JSESSIONID", sessionId);
      cookie.setPath("/");
      cookie.setHttpOnly(true);
      response.setHeader("Set-Cookie",
          "JSESSIONID=" + sessionId + "; Path=/; HttpOnly; SameSite=None; Secure");

      Map<String, Object> responseMap = new HashMap<>();
      responseMap.put("message", "Login successful");
      responseMap.put("user", identifier);

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