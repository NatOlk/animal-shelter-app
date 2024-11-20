package com.ansh.auth.controller;

import com.ansh.auth.service.JwtService;
import com.ansh.auth.service.UserProfileService;
import com.ansh.entity.animal.UserProfile;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final static Logger LOG = LoggerFactory.getLogger(AuthController.class);

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private UserProfileService userProfileService;

  @Autowired
  private JwtService jwtService;

  @PostMapping("/login")
  public ResponseEntity<Object> login(@RequestParam String identifier,
      @RequestParam String password,
      HttpServletRequest request) {
    try {
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(identifier, password)
      );

      SecurityContextHolder.getContext().setAuthentication(authentication);

      Map<String, Object> responseMap = new HashMap<>();
      responseMap.put("token", jwtService.generateToken(authentication.getName()));

      Optional<UserProfile> userProfileOtp = userProfileService.findAuthenticatedUser();

      if (userProfileOtp.isPresent()) {
        responseMap.put("email", userProfileOtp.get().getEmail());
        LOG.info("Success");
        return ResponseEntity.ok(responseMap);
      }
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User profile not found");
    } catch (Exception e) {
      LOG.info("Unauthorized");
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }
  }

  @PostMapping("/logout")
  public ResponseEntity<String> logout(HttpSession session) {
    session.invalidate();
    return ResponseEntity.ok("Logout successful");
  }
}