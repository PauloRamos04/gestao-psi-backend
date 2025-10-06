package com.gestaopsi.prd.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * Configuração de Cache para reduzir consultas ao banco
 * e otimizar uso de memória
 */
@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        
        cacheManager.setCaches(Arrays.asList(
            // Cache de pacientes (30 minutos)
            new ConcurrentMapCache("pacientes"),
            
            // Cache de sessões (15 minutos)
            new ConcurrentMapCache("sessoes"),
            
            // Cache de salas (1 hora - muda pouco)
            new ConcurrentMapCache("salas"),
            
            // Cache de mensagens (5 minutos)
            new ConcurrentMapCache("mensagens"),
            
            // Cache de tipos e categorias (24 horas - praticamente estático)
            new ConcurrentMapCache("tipos")
        ));
        
        return cacheManager;
    }
}

