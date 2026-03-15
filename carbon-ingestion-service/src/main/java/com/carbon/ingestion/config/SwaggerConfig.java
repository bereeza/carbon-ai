package com.carbon.ingestion.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Carbon Ingestion Service API")
                        .version("0.1"))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Local development")
                ));
    }
}
