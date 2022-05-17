package com.jobrunr.config;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Configuration of swagger.
 * Swagger config will be used both for exporting Swagger UI and for OpenAPI specification generation.
 */
@Configuration(value = "JobApiSwaggerConfig")
public class SwaggerConfig {

    /**
     * Example Action Executor API
     */
    @Bean
    public GroupedOpenApi schedulerApi() {
        final String[] packagesToScan = {"com.jobrunr.controller"};
        return GroupedOpenApi
                .builder()
                .group("Job Scheduler API")
                .packagesToScan(packagesToScan)
                .pathsToMatch("/v1/job/**")
                .addOpenApiCustomiser(schedulerApiCustomizer())
                .build();
    }

    private OpenApiCustomiser schedulerApiCustomizer() {
        return openAPI -> openAPI
                .info(new Info()
                        .title("Job Scheduler Example API")
                        .description("This is a demo of JobRunr using SpringBoot")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Essential Programming")
                                .email("razvan.prichici@gmail.com")));


    }
}