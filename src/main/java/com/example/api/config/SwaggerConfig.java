package com.example.api.config;

import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;


/**
 * Configuration of swagger.
 * Swagger config will be used both for exporting Swagger UI and for OpenAPI specification generation.
 */
@Configuration
public class SwaggerConfig {

    /**
     * User API
     */
    @Bean
    public GroupedOpenApi exampleApi() {
        final String[] packagesToScan = {"com.example.api.controller"};
        return GroupedOpenApi
                .builder()
                .group("Example API")
                .packagesToScan(packagesToScan)
                .pathsToMatch("/v1/**")
                .addOpenApiCustomiser(exampleApiCustomizer())
                .build();
    }

    private OpenApiCustomiser exampleApiCustomizer() {
        return openAPI -> openAPI
                .info(new Info()
                        .title("Action Executor Example API")
                        .description("This is a sample Spring Boot RESTful service using OpenAPI")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Essential Programming")
                                .email("razvan.prichici@avangarde-software.com")));


    }
}