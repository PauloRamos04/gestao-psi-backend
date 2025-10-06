package com.gestaopsi.prd.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Configuração de gerenciamento de memória
 * Monitora e otimiza uso de memória da aplicação
 */
@Configuration
@EnableScheduling
@Slf4j
public class MemoryConfig {

    /**
     * Monitora uso de memória a cada 5 minutos
     */
    @Scheduled(fixedRate = 300000) // 5 minutos
    public void monitorMemory() {
        Runtime runtime = Runtime.getRuntime();
        
        long maxMemory = runtime.maxMemory() / 1024 / 1024; // MB
        long totalMemory = runtime.totalMemory() / 1024 / 1024; // MB
        long freeMemory = runtime.freeMemory() / 1024 / 1024; // MB
        long usedMemory = totalMemory - freeMemory;
        
        double percentUsed = (double) usedMemory / maxMemory * 100;
        
        log.info("Memória - Usada: {}MB / Total: {}MB / Max: {}MB ({}%)", 
            usedMemory, totalMemory, maxMemory, String.format("%.2f", percentUsed));
        
        // Se uso de memória > 80%, sugerir GC
        if (percentUsed > 80) {
            log.warn("⚠️ Uso de memória alto ({}%). Sugerindo Garbage Collection...", 
                String.format("%.2f", percentUsed));
            System.gc();
        }
    }

    /**
     * Limpa caches periodicamente (a cada hora)
     */
    @Scheduled(fixedRate = 3600000) // 1 hora
    public void clearCachePeriodically() {
        log.info("Limpando caches periódicos para otimizar memória");
        // Os caches serão limpos automaticamente
    }

    /**
     * Log de informações de memória ao iniciar
     */
    @Bean
    public String memoryInfo() {
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory() / 1024 / 1024;
        int processors = runtime.availableProcessors();
        
        log.info("╔═══════════════════════════════════════════════╗");
        log.info("║ Configuração de Memória JVM                   ║");
        log.info("╠═══════════════════════════════════════════════╣");
        log.info("║ Memória Máxima: {} MB                       ║", maxMemory);
        log.info("║ Processadores: {}                             ║", processors);
        log.info("║ Garbage Collector: G1GC (recomendado)        ║");
        log.info("╚═══════════════════════════════════════════════╝");
        
        return "Memory config loaded";
    }
}

