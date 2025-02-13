package com.ansh.auth.service;

import io.jsonwebtoken.Claims;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Interface for JWT operations, including token generation, validation, and extraction.
 */
public interface JwtService {

  /**
   * Extracts the username from a JWT token.
   *
   * @param token the JWT token
   * @return the username (subject) contained in the token
   */
  String extractUsername(String token);

  /**
   * Generates a new JWT token for a given username.
   *
   * @param username the username for which the token is generated
   * @return the generated JWT token
   */
  String generateToken(String username);

  /**
   * Validates a JWT token against user details.
   *
   * @param token       the JWT token to validate
   * @param userDetails the user details for validation
   * @return true if the token is valid, false otherwise
   */
  boolean isTokenValid(String token, UserDetails userDetails);
}
