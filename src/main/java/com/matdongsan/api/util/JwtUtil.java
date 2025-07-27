package com.matdongsan.api.util;

import com.matdongsan.api.security.UserRole;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

  @Value("${security.google.secret-key}")
  private String secret;

  // access 24시간 / refresh 7일
  @Getter
  private final long accessTokenValidity = 1000L * 60 * 60 * 24;     // 24시간
  @Getter
  private final long refreshTokenValidity = 1000L * 60 * 60 * 24 * 7;// 7일

  private Key getSigningKey() {
    return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  }

  /** Access Token 생성 */
  public String generateAccessToken(Long userId, String email, String role) {
    return createToken(userId, email, role, accessTokenValidity);
  }

  /** Refresh Token 생성 */
  public String generateRefreshToken(Long userId, String email, String role) {
    return createToken(userId, email, role, refreshTokenValidity);
  }

  /** JWT 생성 내부 공통 메서드 */
  private String createToken(Long userId, String email, String role, long expiryMillis) {
    Date now = new Date();
    Date expiry = new Date(now.getTime() + expiryMillis);

    return Jwts.builder()
            .setSubject(String.valueOf(userId))
            .claim("email", email)
            .claim("role", role)
            .setIssuedAt(now)
            .setExpiration(expiry)
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();
  }

  /** 토큰 유효성 검증 */
  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder()
              .setSigningKey(getSigningKey())
              .build()
              .parseClaimsJws(token);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }

  /** 토큰에서 클레임 파싱 */
  public Claims parseToken(String token) {
    return Jwts.parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
  }

  /** 인증 객체 생성 */
  public Authentication getAuthentication(String token) {
    Claims claims = parseToken(token);
    String email = claims.get("email", String.class);
    String role = claims.get("role", String.class);
    Long userId = Long.parseLong(claims.getSubject());

    UserRole principal = new UserRole(userId, email, role);
    List<SimpleGrantedAuthority> authorities =
            List.of(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));

    return new UsernamePasswordAuthenticationToken(principal, null, authorities);
  }

  /** Authorization 헤더에서 토큰 추출 */
  public String resolveToken(HttpServletRequest request) {
    String bearer = request.getHeader("Authorization");
    if (bearer != null && bearer.startsWith("Bearer ")) {
      return bearer.substring(7);
    }
    return null;
  }

  /** 토큰 만료 여부 확인 (예외 던지지 않음) */
  public boolean isTokenExpired(String token) {
    try {
      Date expiration = parseToken(token).getExpiration();
      return expiration.before(new Date());
    } catch (JwtException e) {
      return true;
    }
  }

  /** 토큰에서 유저 ID 추출 */
  public Long extractUserId(String token) {
    return Long.parseLong(parseToken(token).getSubject());
  }

}
