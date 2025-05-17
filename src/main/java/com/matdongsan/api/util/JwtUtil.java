package com.matdongsan.api.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
  @Value("${security.google.secret-key}")
  private String secret;

  /**
   * access token 및 유저 정보 발행 메소드
   * @param userId
   * @param email
   * @param role
   * @return
   */
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

  /**
   * 로그인 한 유저의 정보를 가져오기 위한 parse 메소드
   * @param token
   * @return
   */
  public Claims parseToken(String token) {
    return Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
            .build()
            .parseClaimsJws(token)
            .getBody();
  }
}

