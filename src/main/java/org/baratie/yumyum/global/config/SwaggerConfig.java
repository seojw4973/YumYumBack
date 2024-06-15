package org.baratie.yumyum.global.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    public OpenAPI openAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("Project YumYum")
                        .description("위치 기반 맛집 추천 서비스")
                        .version("1.0.0"));
    }
}
