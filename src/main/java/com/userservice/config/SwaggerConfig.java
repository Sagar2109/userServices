package com.userservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${server.port}")
    private String host;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("User Service API")
                        .version("v1.0.0")
                        .description("Comprehensive API documentation for User Service, including all endpoints, models, and error responses.")
                        .termsOfService("https://yourdomain.com/terms")
                        .contact(new Contact()
                                .name("Support Team")
                                .email("support@yourdomain.com")
                                .url("https://yourdomain.com/support")
                        )
                        .license(new io.swagger.v3.oas.models.info.License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")
                        )
                )
                .servers(List.of(
                        new Server().url("http://localhost:" + host).description("Local server"),
                        new Server().url("https://dev.yourdomain.com").description("Production server")
                ))
                .externalDocs(new io.swagger.v3.oas.models.ExternalDocumentation()
                        .description("Find more info here")
                        .url("https://yourdomain.com/docs")
                );
    }
}
