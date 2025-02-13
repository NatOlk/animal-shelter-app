package com.ansh.auth.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.ansh.auth.service.impl.JwtServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

class JwtServiceTest {

  private static final String SECRET_KEY = "abcdefghijklmnopqrstuvwxyz123456";

  private JwtServiceImpl jwtService;

  @Mock
  private UserDetails userDetails;
  private final String username = "testuser";
  private String token;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    jwtService = new JwtServiceImpl();
    jwtService.setJwtSecretKey(SECRET_KEY);

    token = jwtService.generateToken(username);
  }

  @Test
  void generateToken_ShouldGenerateValidToken() {
    assertNotNull(token, "Token should not be null");
    assertTrue(token.startsWith("eyJ"), "Token should start with 'eyJ'");
  }

  @Test
  void extractUsername_shouldReturnCorrectUsername() {
    String extractedUsername = jwtService.extractUsername(token);
    assertEquals(username, extractedUsername, "Extracted username should match the input username");
  }

  @Test
  void isTokenValid_shouldReturnTrueForValidToken() {
    when(userDetails.getUsername()).thenReturn(username);
    assertTrue(jwtService.isTokenValid(token, userDetails),
        "Token should be valid for the given user details");
  }

  @Test
  void isTokenValid_shouldReturnFalseForInvalidToken() {
    when(userDetails.getUsername()).thenReturn("wronguser");
    assertFalse(jwtService.isTokenValid(token, userDetails),
        "Token should be invalid for the wrong user");
  }

  @Test
  void isTokenExpired_shouldReturnFalseForNonExpiredToken() {
    when(userDetails.getUsername()).thenReturn(username);

    assertTrue(jwtService.isTokenValid(token, userDetails), "Token should not be expired");
  }

  @Test
  void isTokenExpired_shouldThrowExpiredJwtExceptionForExpiredToken() {
    when(userDetails.getUsername()).thenReturn(username);
    String expiredToken = Jwts.builder()
        .setSubject(username)
        .setIssuedAt(new Date(System.currentTimeMillis() - 1000 * 60 * 11))
        .setExpiration(new Date(System.currentTimeMillis() - 1000 * 60 * 10))
        .signWith(jwtService.getSigningKey(), io.jsonwebtoken.SignatureAlgorithm.HS256)
        .compact();

    assertThrows(ExpiredJwtException.class, () -> {
      jwtService.isTokenValid(expiredToken, userDetails);
    });
  }
}
