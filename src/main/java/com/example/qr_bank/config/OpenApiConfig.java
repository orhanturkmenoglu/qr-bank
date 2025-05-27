package com.example.qr_bank.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(buildInfo())
                .externalDocs(buildExternalDocs());
    }

    private Info buildInfo() {
        return new Info()
                .title("QR Bank API")
                .version("v1.0.0")
                .description("QR Bank REST API dokümantasyonu")
                .contact(buildContact())
                .license(buildLicense());
    }

    private Contact buildContact() {
        return new Contact()
                .name("Orhan")
                .email("orhan@example.com")
                .url("https://yourwebsite.com");
    }

    private License buildLicense() {
        return new License()
                .name("Apache 2.0")
                .url("http://springdoc.org");
    }

    private ExternalDocumentation buildExternalDocs() {
        return new ExternalDocumentation()
                .description("Proje Kaynak Kodu")
                .url("https://github.com/orhanturkmenoglu/qr-bank");
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("qr-bank-public")
                .pathsToMatch("/api/v1/**")
                .build();
    }

}
