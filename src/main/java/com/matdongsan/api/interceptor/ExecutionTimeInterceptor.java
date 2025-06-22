package com.matdongsan.api.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class ExecutionTimeInterceptor implements HandlerInterceptor {

  private static final String START_TIME_ATTR = "startTime";

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
    request.setAttribute(START_TIME_ATTR, System.currentTimeMillis());
    return true;
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                              Object handler, Exception ex) {
    Long startTime = (Long) request.getAttribute(START_TIME_ATTR);
    if (startTime != null) {
      long duration = System.currentTimeMillis() - startTime;
      String method = request.getMethod();
      String uri = request.getRequestURI();
      int status = response.getStatus();

      log.info("excecuted time: [{}] {} responded with status {} in {} ms", method, uri, status, duration);
    }
  }
}
