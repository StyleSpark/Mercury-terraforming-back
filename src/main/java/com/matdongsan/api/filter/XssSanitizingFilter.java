package com.matdongsan.api.filter;

import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.stereotype.Component;

@Component
public class XssSanitizingFilter implements Filter {

  private String stripXss(String input) {
    if (input == null) return null;
    return input
            .replaceAll("<", "&lt;")
            .replaceAll(">", "&gt;")
            .replaceAll("\"", "&quot;")
            .replaceAll("'", "&#39;");
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
          throws IOException, ServletException {

    HttpServletRequest req = (HttpServletRequest) request;
    chain.doFilter(new XssRequestWrapper(req), response);
  }

  static class XssRequestWrapper extends HttpServletRequestWrapper {
    public XssRequestWrapper(HttpServletRequest request) {
      super(request);
    }

    @Override
    public String getParameter(String name) {
      return stripXss(super.getParameter(name));
    }

    @Override
    public String[] getParameterValues(String name) {
      String[] values = super.getParameterValues(name);
      if (values == null) return null;

      String[] sanitized = new String[values.length];
      for (int i = 0; i < values.length; i++) {
        sanitized[i] = stripXss(values[i]);
      }
      return sanitized;
    }

    private String stripXss(String value) {
      if (value == null) return null;
      return value
              .replaceAll("<", "&lt;")
              .replaceAll(">", "&gt;")
              .replaceAll("\"", "&quot;")
              .replaceAll("'", "&#39;");
    }
  }
}
