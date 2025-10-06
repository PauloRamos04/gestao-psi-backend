package com.gestaopsi.prd.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Configuração de processamento assíncrono
 * Otimiza uso de threads e memória
 */
@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        
        // Pool mínimo de threads
        executor.setCorePoolSize(2);
        
        // Máximo de threads
        executor.setMaxPoolSize(5);
        
        // Capacidade da fila
        executor.setQueueCapacity(100);
        
        // Prefixo do nome das threads
        executor.setThreadNamePrefix("GestaoPsi-Async-");
        
        // Aguardar tasks completarem no shutdown
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        
        executor.initialize();
        return executor;
    }
}

