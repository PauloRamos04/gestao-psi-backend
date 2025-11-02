package com.gestaopsi.prd.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

/**
 * Configuração para definir o timezone padrão do sistema
 * Define como America/Sao_Paulo (UTC-3) para garantir que todas as datas
 * sejam tratadas no horário de Brasília
 */
@Configuration
public class TimezoneConfig {

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));
        System.setProperty("user.timezone", "America/Sao_Paulo");
    }
}

