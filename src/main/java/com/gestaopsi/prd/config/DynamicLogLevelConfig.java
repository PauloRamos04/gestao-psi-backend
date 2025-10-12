package com.gestaopsi.prd.config;

import com.gestaopsi.prd.service.SystemConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Componente que gerencia o nível de log dinamicamente baseado no modo debug
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DynamicLogLevelConfig {

    private final SystemConfigService systemConfigService;
    private final LoggingSystem loggingSystem;
    private Boolean lastDebugModeState = null;

    /**
     * Verifica o modo debug na inicialização do contexto
     */
    @EventListener(ContextRefreshedEvent.class)
    public void onApplicationEvent() {
        applyLogLevel();
    }

    /**
     * Verifica periodicamente se o modo debug mudou (a cada 30 segundos)
     */
    @Scheduled(fixedRate = 30000)
    public void checkDebugMode() {
        applyLogLevel();
    }

    /**
     * Aplica o nível de log baseado no modo debug
     */
    private void applyLogLevel() {
        try {
            Boolean debugMode = systemConfigService.isDebugMode();
            
            // Só altera se o estado mudou
            if (lastDebugModeState == null || !lastDebugModeState.equals(debugMode)) {
                if (debugMode != null && debugMode) {
                    enableDebugMode();
                } else {
                    disableDebugMode();
                }
                lastDebugModeState = debugMode;
            }
        } catch (Exception e) {
            log.error("Erro ao verificar modo debug", e);
        }
    }

    /**
     * Ativa o modo debug (logs detalhados)
     */
    private void enableDebugMode() {
        log.warn("========================================");
        log.warn("🐛 MODO DEBUG ATIVADO");
        log.warn("========================================");
        
        // Alterar nível de log para DEBUG
        loggingSystem.setLogLevel("com.gestaopsi.prd", LogLevel.DEBUG);
        loggingSystem.setLogLevel("org.springframework.web", LogLevel.DEBUG);
        loggingSystem.setLogLevel("org.springframework.security", LogLevel.DEBUG);
        loggingSystem.setLogLevel("org.hibernate.SQL", LogLevel.DEBUG);
        loggingSystem.setLogLevel("org.hibernate.type.descriptor.sql.BasicBinder", LogLevel.TRACE);
        
        log.debug("Níveis de log alterados para DEBUG");
        log.debug("Pacotes afetados: com.gestaopsi.prd, spring.web, spring.security, hibernate");
    }

    /**
     * Desativa o modo debug (logs normais)
     */
    private void disableDebugMode() {
        log.warn("========================================");
        log.warn("✅ MODO DEBUG DESATIVADO");
        log.warn("========================================");
        
        // Restaurar nível de log para ERROR (conforme application.properties)
        loggingSystem.setLogLevel("com.gestaopsi.prd", LogLevel.ERROR);
        loggingSystem.setLogLevel("org.springframework.web", LogLevel.ERROR);
        loggingSystem.setLogLevel("org.springframework.security", LogLevel.ERROR);
        loggingSystem.setLogLevel("org.hibernate.SQL", LogLevel.ERROR);
        loggingSystem.setLogLevel("org.hibernate.type.descriptor.sql.BasicBinder", LogLevel.ERROR);
        
        log.info("Níveis de log restaurados para ERROR");
    }
    
    /**
     * Método público para forçar aplicação do nível de log
     */
    public void forceApplyLogLevel() {
        lastDebugModeState = null;
        applyLogLevel();
    }
}

