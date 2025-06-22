package com.matdongsan.api.security;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

@ControllerAdvice
public class XssRequestBodyAdvice extends RequestBodyAdviceAdapter {

  @Override
  public boolean supports(MethodParameter methodParameter, Type targetType,
                          Class<? extends HttpMessageConverter<?>> converterType) {
    return true;
  }

  @Override
  public Object afterBodyRead(Object body, HttpInputMessage inputMessage,
                              MethodParameter parameter, Type targetType,
                              Class<? extends HttpMessageConverter<?>> converterType) {

    sanitizeObject(body);
    return body;
  }

  private void sanitizeObject(Object obj) {
    if (obj == null) return;

    Field[] fields = obj.getClass().getDeclaredFields();
    for (Field field : fields) {
      if (field.getType() == String.class) {
        field.setAccessible(true);
        try {
          String value = (String) field.get(obj);
          if (value != null) {
            String sanitized = stripXss(value);
            field.set(obj, sanitized);
          }
        } catch (IllegalAccessException ignored) {}
      }
    }
  }

  private String stripXss(String input) {
    return input
            .replaceAll("<", "&lt;")
            .replaceAll(">", "&gt;")
            .replaceAll("\"", "&quot;")
            .replaceAll("'", "&#39;");
  }
}
