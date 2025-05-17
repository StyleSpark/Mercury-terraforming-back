package com.matdongsan.api.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
  private final String secret = "b9F7r4LZtYkMv1xWcS8dU2QoEjNaP6Rh"; // 환경 변수로 분리 해야함

  public String generateToken(Long userId, String email, String role) {
    Date now = new Date();
    Date expiry = new Date(now.getTime() + 1000 * 60 * 60 * 24); // 1일

    return Jwts.builder()
            .setSubject(String.valueOf(userId))
            .claim("email", email)
            .claim("role", role)
            .setIssuedAt(now)
            .setExpiration(expiry)
            .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
            .compact();
  }

  public Claims parseToken(String token) {
    return Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
            .build()
            .parseClaimsJws(token)
            .getBody();
  }
}

