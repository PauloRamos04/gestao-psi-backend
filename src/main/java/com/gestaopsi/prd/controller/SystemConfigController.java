package com.gestaopsi.prd.controller;

import com.gestaopsi.prd.dto.SystemConfigRequest;
import com.gestaopsi.prd.dto.SystemConfigResponse;
import com.gestaopsi.prd.service.SystemConfigService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/system-config")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class SystemConfigController {

    private final SystemConfigService systemConfigService;

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<SystemConfigResponse> getAllConfigs() {
        log.info("Buscando todas as configurações do sistema");
        SystemConfigResponse response = systemConfigService.getAllConfigs();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/system")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Map<String, String>> updateSystemConfigs(@Valid @RequestBody SystemConfigRequest request) {
        log.info("Atualizando configurações do sistema");
        systemConfigService.updateSystemConfigs(request);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Configurações do sistema atualizadas com sucesso");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/email")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Map<String, String>> updateEmailConfigs(@Valid @RequestBody SystemConfigRequest request) {
        log.info("Atualizando configurações de email");
        systemConfigService.updateEmailConfigs(request);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Configurações de email atualizadas com sucesso");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/security")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Map<String, String>> updateSecurityConfigs(@Valid @RequestBody SystemConfigRequest request) {
        log.info("Atualizando configurações de segurança");
        systemConfigService.updateSecurityConfigs(request);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Configurações de segurança atualizadas com sucesso");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/notifications")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Map<String, String>> updateNotificationConfigs(@Valid @RequestBody SystemConfigRequest request) {
        log.info("Atualizando configurações de notificações");
        systemConfigService.updateNotificationConfigs(request);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Configurações de notificações atualizadas com sucesso");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/email/test")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Map<String, String>> testEmailConnection() {
        log.info("Testando conexão de email");
        try {
            systemConfigService.testEmailConnection();
            Map<String, String> response = new HashMap<>();
            response.put("message", "Conexão de email testada com sucesso");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Erro ao testar conexão de email", e);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Erro ao testar conexão: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/initialize")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Map<String, String>> initializeDefaultConfigs() {
        log.info("Inicializando configurações padrão");
        systemConfigService.initializeDefaultConfigs();
        Map<String, String> response = new HashMap<>();
        response.put("message", "Configurações padrão inicializadas com sucesso");
        return ResponseEntity.ok(response);
    }
}

