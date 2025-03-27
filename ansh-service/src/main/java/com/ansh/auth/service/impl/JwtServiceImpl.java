package com.ansh.auth.service.impl;

import com.ansh.auth.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Service
public class JwtServiceImpl implements JwtService {

  @Value("${jwt.secret.key}")
  private String jwtSecretKey;

  @Override
  public String generateToken(Authentication authentication) {
    CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

    Map<String, Object> claims = new HashMap<>();
    claims.put("id", userDetails.getId());
    claims.put("email", userDetails.getEmail());
    claims.put("name", userDetails.getName());
    claims.put("roles", userDetails.getRoleNames());

    return Jwts.builder()
        .setClaims(claims)
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
        .signWith(getSigningKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  @Override
  public boolean isTokenValid(String token) {
    try {
      extractAllClaims(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  @Override
  public List<String> extractRoles(String token) {
    return extractClaim(token, claims -> claims.get("roles", List.class));
  }

  @Override
  public String extractEmail(String token) {
    return extractAllClaims(token).get("email", String.class);
  }

  @Override
  public String extractName(String token) {
    return extractAllClaims(token).get("name", String.class);
  }

  @Override
  public Long extractId(String token) {
    return extractAllClaims(token).get("id", Long.class);
  }

  private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  protected SecretKey getSigningKey() {
    return Keys.hmacShaKeyFor(jwtSecretKey.getBytes());
  }

  protected void setJwtSecretKey(String jwtSecretKey) {
    this.jwtSecretKey = jwtSecretKey;
  }
}
