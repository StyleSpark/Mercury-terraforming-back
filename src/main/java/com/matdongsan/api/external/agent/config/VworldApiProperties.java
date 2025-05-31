package com.matdongsan.api.external.agent.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "vworld.api")
public class VworldApiProperties {
  private String key;
  private String baseUrl;
}
