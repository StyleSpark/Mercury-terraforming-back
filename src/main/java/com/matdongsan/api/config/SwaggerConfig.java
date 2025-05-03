package com.matdongsan.api.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
            .components(new Components())
            .info(apiInfo());
  }

  private Info apiInfo() {
    return new Info()
            .title("Mercury-terraforming-project") // API의 제목
            .description("일반 유저와 중개인 모두가 매물을 등록하고 거래할 수 있는 중간 플렛폼 입니다. 또한 대리 서비스와 커뮤니티를 통해 방을 구하고 정보를 공유 할 수 있습니다.") // API에 대한 설명
            .version("1.0.0"); // API의 버전
  }
}

//jwt 사용하는 경우
/*
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        String jwt = "JWT";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwt);
        Components components = new Components().addSecuritySchemes(jwt, new SecurityScheme()
                .name(jwt)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
        );
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo())
                .addSecurityItem(securityRequirement)
                .components(components);
    }
    private Info apiInfo() {
        return new Info()
                .title("API Test") // API의 제목
                .description("Let's practice Swagger UI") // API에 대한 설명
                .version("1.0.0"); // API의 버전
    }
}
 */