package com.matdongsan.api.config;

import com.matdongsan.api.interceptor.LoggingInterceptor;
import com.matdongsan.api.interceptor.ExecutionTimeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
            .allowedOrigins("http://localhost:3000")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
            .allowCredentials(true);
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    // 요청 로깅 인터셉터
    registry.addInterceptor(new LoggingInterceptor())
            .addPathPatterns("/api/**")
            .excludePathPatterns(
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/favicon.ico",
                    "/error"
            );

    // 실행 시간 측정 인터셉터
    registry.addInterceptor(new ExecutionTimeInterceptor())
            .addPathPatterns("/api/**")
            .excludePathPatterns(
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/favicon.ico",
                    "/error"
            );
  }
}
