package com.matdongsan.api.util;

import com.matdongsan.api.security.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
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

  private final long expirationMs = 1000 * 60 * 60 * 24; // 24시간

  private Key getSigningKey() {
    return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  }

  /** 토큰 생성 */
  public String generateToken(Long userId, String email, String role) {
    Date now = new Date();
    Date expiry = new Date(now.getTime() + expirationMs);

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

  /** 토큰에서 이메일, 역할 등 추출 */
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

    // DB 조회 없이도 토큰 내용이 충분히 신뢰 가능하다면 이 부분 생략 가능 - 고민중
    // UserVO user = userMapper.findByEmail(email);

    UserRole principal = new UserRole(userId, email, role);

    List<SimpleGrantedAuthority> authorities =
            List.of(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));

    return new UsernamePasswordAuthenticationToken(principal, null, authorities);
  }

  /** 헤더에서 토큰 추출 */
  public String resolveToken(HttpServletRequest request) {
    String bearer = request.getHeader("Authorization");
    if (bearer != null && bearer.startsWith("Bearer ")) {
      return bearer.substring(7);
    }
    return null;
  }
}
