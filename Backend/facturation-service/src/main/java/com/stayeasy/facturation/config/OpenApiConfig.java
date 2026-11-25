package com.stayeasy.facturation.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info()
                .title("Facturation")
                .version("1.0.0")
                .description("Microservice Facturation de la plateforme StayEasy"));
    }
}
