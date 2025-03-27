package com.ansh.auth.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JwtServiceTest {

  private static final String SECRET_KEY = "abcdefghijklmnopqrstuvwxyz123456";

  private JwtServiceImpl jwtService;

  private final String username = "testuser";
  private String token;

  @BeforeEach
  void setUp() {
    jwtService = new JwtServiceImpl();
    jwtService.setJwtSecretKey(SECRET_KEY);

    token = Jwts.builder()
        .setSubject(username)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10))
        .signWith(jwtService.getSigningKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  @Test
  void generateToken_shouldGenerateValidToken() {
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
    assertTrue(jwtService.isTokenValid(token), "Token should be valid");
  }

  @Test
  void isTokenValid_shouldReturnFalseForTamperedToken() {
    String tamperedToken = STR."\{token.substring(0, token.length() - 2)}xx";
    assertFalse(jwtService.isTokenValid(tamperedToken), "Token should be invalid when tampered");
  }

  @Test
  void isTokenValid_shouldReturnFalseForExpiredToken() {
    String expiredToken = Jwts.builder()
        .setSubject(username)
        .setIssuedAt(new Date(System.currentTimeMillis() - 1000 * 60 * 11))
        .setExpiration(new Date(System.currentTimeMillis() - 1000 * 60 * 10))
        .signWith(jwtService.getSigningKey(), SignatureAlgorithm.HS256)
        .compact();

    assertFalse(jwtService.isTokenValid(expiredToken), "Expired token should be invalid");
  }

  @Test
  void extractRoles_shouldReturnCorrectRoles() {
    String tokenWithRoles = Jwts.builder()
        .setSubject(username)
        .claim("roles", List.of("ADMIN", "EMPLOYEE"))
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10))
        .signWith(jwtService.getSigningKey(), SignatureAlgorithm.HS256)
        .compact();

    var roles = jwtService.extractRoles(tokenWithRoles);

    assertNotNull(roles, "Roles should not be null");
    assertEquals(2, roles.size(), "Should contain two roles");
    assertTrue(roles.contains("ADMIN"), "Should contain ADMIN");
    assertTrue(roles.contains("EMPLOYEE"), "Should contain EMPLOYEE");
  }
}
