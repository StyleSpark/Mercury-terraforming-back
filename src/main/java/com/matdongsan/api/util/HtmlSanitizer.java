package com.matdongsan.api.util;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

public class HtmlSanitizer {

  /**
   * 사용자 HTML 콘텐츠를 XSS-safe하게 정제합니다.
   * <p>, <img>, <strong>, <ul> 등은 허용되며, script, onerror, javascript: 등은 제거됩니다.
   */
  public static String sanitize(String rawHtml) {
    if (rawHtml == null) return null;
    // base()
    //basicWithImages()
    //none()
    // simpleText()
    // relaxed() = 대부분의 안전한 HTML 허용
    return Jsoup.clean(rawHtml, Safelist.relaxed()
            .addTags("img", "br") // 이미지,개행 허용
            .addAttributes("img", "src", "alt", "title", "width", "height") // img에 필요한 속성
            .addProtocols("img", "src", "http", "https", "data") // base64 src도 허용
    );
  }
}
