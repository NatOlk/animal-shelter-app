package com.ansh.auth.service;

import java.util.List;
import org.springframework.security.core.Authentication;

/**
 * Interface for JWT operations, including token generation, validation, and claim extraction.
 */
public interface JwtService {

  /**
   * Extracts the username (usually the subject) from the JWT token.
   *
   * @param token the JWT token
   * @return the username stored in the token
   */
  String extractUsername(String token);

  /**
   * Generates a new JWT token based on the given authentication object. Claims such as email, name,
   * roles, and ID should be included in the token.
   *
   * @param authentication the authenticated user information
   * @return the generated JWT token as a String
   */
  String generateToken(Authentication authentication);

  /**
   * Validates the given JWT token by checking its signature and expiration.
   *
   * @param token the JWT token
   * @return true if the token is valid and not expired, false otherwise
   */
  boolean isTokenValid(String token);

  /**
   * Extracts the list of role names from the JWT token.
   *
   * @param token the JWT token
   * @return a list of role names (e.g., ["ADMIN", "EMPLOYEE"])
   */
  List<String> extractRoles(String token);

  /**
   * Extracts the email address from the JWT token claims.
   *
   * @param token the JWT token
   * @return the email stored in the token
   */
  String extractEmail(String token);

  /**
   * Extracts the full name of the user from the JWT token claims.
   *
   * @param token the JWT token
   * @return the name stored in the token
   */
  String extractName(String token);

  /**
   * Extracts the user ID from the JWT token claims.
   *
   * @param token the JWT token
   * @return the ID stored in the token
   */
  Long extractId(String token);
}