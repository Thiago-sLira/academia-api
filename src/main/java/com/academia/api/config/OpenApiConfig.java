package com.academia.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Value("${spring.application.version}")
    private String appVersion;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Gestão de Academia")
                        .version(appVersion)
                        .description("API REST para gerenciamento de alunos, fichas e planos de treino.")
                        .contact(new Contact()
                                .name("Suporte Faculdade")
                                .email("contato@academia.com")));
    }
}