package com.matdongsan.api.config;

import com.matdongsan.api.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
            .cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                    // 정적 리소스 및 공개 API 허용
                    .requestMatchers(
                            "/", "/index.html", "/favicon.ico",
                            "/assets/**", "/images/**", "/css/**", "/js/**",
                            "/swagger-ui/**", "/v3/api-docs/**",
                            "/api/auth/**"
                    ).permitAll()

                    // GET 요청 중에서 admin, user는 인증 필요
                    .requestMatchers(HttpMethod.GET, "/api/admin/**", "/api/user/**").authenticated()

                    // 나머지 모든 GET 요청은 허용
                    .requestMatchers(HttpMethod.GET, "/api/**").permitAll()

                    // 나머지 요청 (POST, PUT 등)은 인증 필요
                    .requestMatchers("/api/**").authenticated()

                    // 나머지는 허용 (프론트 라우팅 등)
                    .anyRequest().permitAll()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
