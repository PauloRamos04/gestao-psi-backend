package com.gestaopsi.prd.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Gestão PSI API")
                        .description("Sistema de Gestão para Clínicas de Psicologia")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Gestão PSI")
                                .email("contato@gestaopsi.com")));
    }
}
