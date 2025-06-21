package com.matdongsan.api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI openAPI() {
    final String JWT_SCHEME_NAME = "JWT";

    return new OpenAPI()
            .info(apiInfo())
            .addSecurityItem(new SecurityRequirement().addList(JWT_SCHEME_NAME))
            .components(new Components()
                    .addSecuritySchemes(JWT_SCHEME_NAME,
                            new SecurityScheme()
                                    .name(JWT_SCHEME_NAME)
                                    .type(SecurityScheme.Type.HTTP)
                                    .scheme("bearer")
                                    .bearerFormat("JWT")));
  }

  private Info apiInfo() {
    return new Info()
            .title("Mercury-terraforming-project")
            .description("일반 유저와 중개인 모두가 매물을 등록하고 거래할 수 있는 중간 플랫폼입니다. 또한 커뮤니티를 통해 방을 구하고 정보를 공유할 수 있습니다.")
            .version("1.0.0");
  }
}
