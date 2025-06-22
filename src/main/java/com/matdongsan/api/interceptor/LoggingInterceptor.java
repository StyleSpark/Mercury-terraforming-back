package com.matdongsan.api.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class LoggingInterceptor implements HandlerInterceptor {

  private static final String START_TIME = "startTime";

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
    long startTime = System.currentTimeMillis();
    request.setAttribute(START_TIME, startTime);

    String method = request.getMethod();
    String uri = request.getRequestURI();
    String ip = request.getRemoteAddr();

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String principal = (auth != null && auth.isAuthenticated() && auth.getPrincipal() != "anonymousUser")
            ? auth.getName()
            : "Anonymous";

    log.info("logging: [{}] {} from IP={} by={}", method, uri, ip, principal);
    return true;
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                              Object handler, Exception ex) {
    long startTime = (Long) request.getAttribute(START_TIME);
    long duration = System.currentTimeMillis() - startTime;

    String method = request.getMethod();
    String uri = request.getRequestURI();
    int status = response.getStatus();

    log.info("⬅️ [{}] {} completed in {} ms with status={}", method, uri, duration, status);
  }
}
