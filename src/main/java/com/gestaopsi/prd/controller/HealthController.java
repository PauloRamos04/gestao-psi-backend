package com.gestaopsi.prd.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller para monitoramento de saúde e memória
 */
@RestController
@RequestMapping("/health")
@Tag(name = "Health", description = "Monitoramento de saúde e recursos")
@Slf4j
public class HealthController {

    @GetMapping("/memory")
    @Operation(summary = "Verificar uso de memória")
    public ResponseEntity<Map<String, Object>> getMemoryInfo() {
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapUsage = memoryMXBean.getHeapMemoryUsage();
        MemoryUsage nonHeapUsage = memoryMXBean.getNonHeapMemoryUsage();
        
        Runtime runtime = Runtime.getRuntime();
        
        Map<String, Object> memoryInfo = new HashMap<>();
        
        // Heap Memory
        Map<String, Long> heap = new HashMap<>();
        heap.put("init", heapUsage.getInit() / 1024 / 1024);
        heap.put("used", heapUsage.getUsed() / 1024 / 1024);
        heap.put("committed", heapUsage.getCommitted() / 1024 / 1024);
        heap.put("max", heapUsage.getMax() / 1024 / 1024);
        memoryInfo.put("heap", heap);
        
        // Non-Heap Memory (Metaspace)
        Map<String, Long> nonHeap = new HashMap<>();
        nonHeap.put("used", nonHeapUsage.getUsed() / 1024 / 1024);
        nonHeap.put("committed", nonHeapUsage.getCommitted() / 1024 / 1024);
        nonHeap.put("max", nonHeapUsage.getMax() / 1024 / 1024);
        memoryInfo.put("nonHeap", nonHeap);
        
        // Runtime Info
        Map<String, Long> runtimeInfo = new HashMap<>();
        runtimeInfo.put("freeMemory", runtime.freeMemory() / 1024 / 1024);
        runtimeInfo.put("totalMemory", runtime.totalMemory() / 1024 / 1024);
        runtimeInfo.put("maxMemory", runtime.maxMemory() / 1024 / 1024);
        runtimeInfo.put("processors", (long) runtime.availableProcessors());
        memoryInfo.put("runtime", runtimeInfo);
        
        // Porcentagem de uso
        double percentUsed = (double) heapUsage.getUsed() / heapUsage.getMax() * 100;
        memoryInfo.put("percentUsed", String.format("%.2f%%", percentUsed));
        
        // Status
        String status = percentUsed < 70 ? "HEALTHY" : percentUsed < 85 ? "WARNING" : "CRITICAL";
        memoryInfo.put("status", status);
        
        return ResponseEntity.ok(memoryInfo);
    }

    @GetMapping("/gc")
    @Operation(summary = "Forçar Garbage Collection (use com cautela)")
    public ResponseEntity<Map<String, String>> forceGarbageCollection() {
        log.warn("Garbage Collection forçado via endpoint");
        
        long beforeUsed = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        
        System.gc();
        
        try {
            Thread.sleep(1000); // Aguardar GC completar
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        long afterUsed = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long freed = (beforeUsed - afterUsed) / 1024 / 1024;
        
        Map<String, String> result = new HashMap<>();
        result.put("status", "GC executado");
        result.put("memoryFreed", freed + " MB");
        
        return ResponseEntity.ok(result);
    }

    @GetMapping("/status")
    @Operation(summary = "Status geral do sistema")
    public ResponseEntity<Map<String, Object>> getSystemStatus() {
        Map<String, Object> status = new HashMap<>();
        
        status.put("application", "GestaoPsi");
        status.put("version", "2.2.0");
        status.put("status", "UP");
        status.put("timestamp", System.currentTimeMillis());
        
        // Thread info
        status.put("activeThreads", Thread.activeCount());
        
        return ResponseEntity.ok(status);
    }
}

