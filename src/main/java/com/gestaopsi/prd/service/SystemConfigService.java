package com.gestaopsi.prd.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gestaopsi.prd.dto.SystemConfigRequest;
import com.gestaopsi.prd.dto.SystemConfigResponse;
import com.gestaopsi.prd.entity.SystemConfig;
import com.gestaopsi.prd.repository.SystemConfigRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class SystemConfigService {

    private final SystemConfigRepository systemConfigRepository;
    private final ObjectMapper objectMapper;
    
    // Chave simples para criptografia (em produção use um gerenciador de segredos)
    private static final String ENCRYPTION_KEY = "GestaoPsi2024Key"; // 16 bytes
    
    @Transactional
    public void initializeDefaultConfigs() {
        log.info("Verificando configurações padrão do sistema...");
        
        // Configurações de Sistema
        createConfigIfNotExists("system.name", "Gestão PSI", "STRING", "Nome do Sistema", "SYSTEM", false);
        createConfigIfNotExists("system.version", "1.0.0", "STRING", "Versão do Sistema", "SYSTEM", false);
        createConfigIfNotExists("system.maintenanceMode", "false", "BOOLEAN", "Modo de Manutenção", "SYSTEM", false);
        createConfigIfNotExists("system.debugMode", "false", "BOOLEAN", "Modo Debug", "SYSTEM", false);
        
        // Configurações de Email (senha deve ser criptografada)
        createConfigIfNotExists("email.smtpHost", "smtp.gmail.com", "STRING", "Servidor SMTP", "EMAIL", false);
        createConfigIfNotExists("email.smtpPort", "587", "INTEGER", "Porta SMTP", "EMAIL", false);
        createConfigIfNotExists("email.smtpUsername", "", "STRING", "Usuário SMTP", "EMAIL", false);
        createConfigIfNotExists("email.smtpPassword", "", "STRING", "Senha SMTP", "EMAIL", true);
        createConfigIfNotExists("email.smtpFromEmail", "noreply@gestaopsi.com", "STRING", "Email Remetente", "EMAIL", false);
        
        // Configurações de Backup
        createConfigIfNotExists("backup.autoBackup", "true", "BOOLEAN", "Backup Automático", "BACKUP", false);
        createConfigIfNotExists("backup.frequency", "daily", "STRING", "Frequência do Backup", "BACKUP", false);
        createConfigIfNotExists("backup.retention", "30", "INTEGER", "Retenção de Backup (dias)", "BACKUP", false);
        
        // Configurações de Segurança
        createConfigIfNotExists("security.sessionTimeout", "30", "INTEGER", "Timeout da Sessão (minutos)", "SECURITY", false);
        createConfigIfNotExists("security.maxLoginAttempts", "5", "INTEGER", "Máximo de Tentativas de Login", "SECURITY", false);
        createConfigIfNotExists("security.passwordPolicy", 
            "{\"minLength\":8,\"requireUppercase\":true,\"requireNumbers\":true,\"requireSpecialChars\":true}", 
            "JSON", "Política de Senhas", "SECURITY", false);
        
        // Configurações de Notificações
        createConfigIfNotExists("notifications.email", "true", "BOOLEAN", "Notificações por Email", "NOTIFICATIONS", false);
        createConfigIfNotExists("notifications.sms", "false", "BOOLEAN", "Notificações por SMS", "NOTIFICATIONS", false);
        createConfigIfNotExists("notifications.push", "true", "BOOLEAN", "Notificações Push", "NOTIFICATIONS", false);
        
        // Configurações de Logs
        createConfigIfNotExists("logs.level", "INFO", "STRING", "Nível de Log", "LOGS", false);
        createConfigIfNotExists("logs.retention", "90", "INTEGER", "Retenção de Logs (dias)", "LOGS", false);
        createConfigIfNotExists("logs.audit", "true", "BOOLEAN", "Logs de Auditoria", "LOGS", false);
        
        log.info("Configurações padrão verificadas/criadas com sucesso");
    }
    
    private void createConfigIfNotExists(String key, String value, String type, String description, String category, boolean isEncrypted) {
        if (!systemConfigRepository.existsByConfigKey(key)) {
            SystemConfig config = new SystemConfig();
            config.setConfigKey(key);
            config.setConfigValue(isEncrypted ? encrypt(value) : value);
            config.setConfigType(type);
            config.setDescription(description);
            config.setCategory(category);
            config.setIsEncrypted(isEncrypted);
            systemConfigRepository.save(config);
            log.info("Configuração criada: {} = {}", key, isEncrypted ? "***" : value);
        }
    }
    
    public SystemConfigResponse getAllConfigs() {
        SystemConfigResponse response = new SystemConfigResponse();
        
        // Sistema
        response.setSystemName(getConfigValue("system.name", "Gestão PSI"));
        response.setSystemVersion(getConfigValue("system.version", "1.0.0"));
        response.setMaintenanceMode(getBooleanConfig("system.maintenanceMode", false));
        response.setDebugMode(getBooleanConfig("system.debugMode", false));
        
        // Email
        response.setSmtpHost(getConfigValue("email.smtpHost", "smtp.gmail.com"));
        response.setSmtpPort(getIntegerConfig("email.smtpPort", 587));
        response.setSmtpUsername(getConfigValue("email.smtpUsername", ""));
        response.setSmtpPassword(getEncryptedConfigValue("email.smtpPassword", ""));
        response.setSmtpFromEmail(getConfigValue("email.smtpFromEmail", "noreply@gestaopsi.com"));
        
        // Backup
        response.setAutoBackup(getBooleanConfig("backup.autoBackup", true));
        response.setBackupFrequency(getConfigValue("backup.frequency", "daily"));
        response.setBackupRetention(getIntegerConfig("backup.retention", 30));
        
        // Segurança
        response.setSessionTimeout(getIntegerConfig("security.sessionTimeout", 30));
        response.setMaxLoginAttempts(getIntegerConfig("security.maxLoginAttempts", 5));
        
        // Política de Senhas
        String passwordPolicyJson = getConfigValue("security.passwordPolicy", 
            "{\"minLength\":8,\"requireUppercase\":true,\"requireNumbers\":true,\"requireSpecialChars\":true}");
        try {
            Map<String, Object> policyMap = objectMapper.readValue(passwordPolicyJson, new TypeReference<Map<String, Object>>() {});
            SystemConfigResponse.PasswordPolicyResponse passwordPolicy = new SystemConfigResponse.PasswordPolicyResponse();
            passwordPolicy.setMinLength((Integer) policyMap.get("minLength"));
            passwordPolicy.setRequireUppercase((Boolean) policyMap.get("requireUppercase"));
            passwordPolicy.setRequireNumbers((Boolean) policyMap.get("requireNumbers"));
            passwordPolicy.setRequireSpecialChars((Boolean) policyMap.get("requireSpecialChars"));
            response.setPasswordPolicy(passwordPolicy);
        } catch (Exception e) {
            log.error("Erro ao parsear política de senhas", e);
            SystemConfigResponse.PasswordPolicyResponse defaultPolicy = new SystemConfigResponse.PasswordPolicyResponse();
            defaultPolicy.setMinLength(8);
            defaultPolicy.setRequireUppercase(true);
            defaultPolicy.setRequireNumbers(true);
            defaultPolicy.setRequireSpecialChars(true);
            response.setPasswordPolicy(defaultPolicy);
        }
        
        // Notificações
        response.setEmailNotifications(getBooleanConfig("notifications.email", true));
        response.setSmsNotifications(getBooleanConfig("notifications.sms", false));
        response.setPushNotifications(getBooleanConfig("notifications.push", true));
        
        // Logs
        response.setLogLevel(getConfigValue("logs.level", "INFO"));
        response.setLogRetention(getIntegerConfig("logs.retention", 90));
        response.setAuditLogs(getBooleanConfig("logs.audit", true));
        
        return response;
    }
    
    @Transactional
    public void updateSystemConfigs(SystemConfigRequest request) {
        Map<String, Object> configs = request.getConfigs();
        
        configs.forEach((key, value) -> {
            String fullKey = "system." + key;
            updateConfig(fullKey, value.toString(), false);
        });
        
        // Se atualizou o debugMode, forçar aplicação imediata
        if (configs.containsKey("debugMode")) {
            log.info("Modo debug alterado, aplicando mudanças...");
        }
    }
    
    @Transactional
    public void updateEmailConfigs(SystemConfigRequest request) {
        Map<String, Object> configs = request.getConfigs();
        
        configs.forEach((key, value) -> {
            String fullKey = "email." + key;
            boolean isPassword = key.equals("smtpPassword");
            updateConfig(fullKey, value.toString(), isPassword);
        });
    }
    
    @Transactional
    public void updateSecurityConfigs(SystemConfigRequest request) {
        Map<String, Object> configs = request.getConfigs();
        
        // Separar política de senhas dos outros configs
        Map<String, Object> passwordPolicyMap = new HashMap<>();
        List<String> keysToRemove = new ArrayList<>();
        
        for (Map.Entry<String, Object> entry : configs.entrySet()) {
            String key = entry.getKey();
            if (key.equals("minLength") || key.equals("requireUppercase") || 
                key.equals("requireNumbers") || key.equals("requireSpecialChars")) {
                passwordPolicyMap.put(key, entry.getValue());
                keysToRemove.add(key);
            }
        }
        
        // Remover chaves da política de senhas
        keysToRemove.forEach(configs::remove);
        
        // Atualizar configs normais
        configs.forEach((key, value) -> {
            String fullKey = "security." + key;
            updateConfig(fullKey, value.toString(), false);
        });
        
        // Atualizar política de senhas
        if (!passwordPolicyMap.isEmpty()) {
            try {
                String passwordPolicyJson = objectMapper.writeValueAsString(passwordPolicyMap);
                updateConfig("security.passwordPolicy", passwordPolicyJson, false);
            } catch (Exception e) {
                log.error("Erro ao salvar política de senhas", e);
            }
        }
    }
    
    @Transactional
    public void updateNotificationConfigs(SystemConfigRequest request) {
        Map<String, Object> configs = request.getConfigs();
        
        configs.forEach((key, value) -> {
            String fullKey;
            if (key.equals("emailNotifications")) {
                fullKey = "notifications.email";
            } else if (key.equals("smsNotifications")) {
                fullKey = "notifications.sms";
            } else if (key.equals("pushNotifications")) {
                fullKey = "notifications.push";
            } else if (key.equals("logLevel")) {
                fullKey = "logs.level";
            } else if (key.equals("logRetention")) {
                fullKey = "logs.retention";
            } else if (key.equals("auditLogs")) {
                fullKey = "logs.audit";
            } else {
                return;
            }
            updateConfig(fullKey, value.toString(), false);
        });
    }
    
    private void updateConfig(String key, String value, boolean isEncrypted) {
        SystemConfig config = systemConfigRepository.findByConfigKey(key)
            .orElseGet(() -> {
                log.warn("Configuração não encontrada: {}. Criando nova configuração.", key);
                SystemConfig newConfig = new SystemConfig();
                newConfig.setConfigKey(key);
                newConfig.setConfigType("STRING");
                newConfig.setDescription("Auto-criada");
                newConfig.setCategory(key.split("\\.")[0].toUpperCase());
                newConfig.setIsEncrypted(isEncrypted);
                return newConfig;
            });
        
        if (isEncrypted && value != null && !value.isEmpty()) {
            config.setConfigValue(encrypt(value));
        } else {
            config.setConfigValue(value);
        }
        
        config = systemConfigRepository.save(config);
        log.info("Configuração {} [ID: {}]: {} = {}", 
                 config.getId() == null ? "criada" : "atualizada",
                 config.getId(), 
                 key, 
                 isEncrypted ? "***" : value);
    }
    
    private String getConfigValue(String key, String defaultValue) {
        return systemConfigRepository.findByConfigKey(key)
            .map(SystemConfig::getConfigValue)
            .orElse(defaultValue);
    }
    
    private String getEncryptedConfigValue(String key, String defaultValue) {
        return systemConfigRepository.findByConfigKey(key)
            .map(config -> {
                if (config.getIsEncrypted() && config.getConfigValue() != null && !config.getConfigValue().isEmpty()) {
                    return decrypt(config.getConfigValue());
                }
                return config.getConfigValue();
            })
            .orElse(defaultValue);
    }
    
    private Boolean getBooleanConfig(String key, Boolean defaultValue) {
        String value = getConfigValue(key, defaultValue.toString());
        return Boolean.parseBoolean(value);
    }
    
    private Integer getIntegerConfig(String key, Integer defaultValue) {
        String value = getConfigValue(key, defaultValue.toString());
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    // Métodos simples de criptografia (em produção use algo mais robusto)
    private String encrypt(String value) {
        if (value == null || value.isEmpty()) {
            return value;
        }
        try {
            SecretKeySpec key = new SecretKeySpec(ENCRYPTION_KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encrypted = cipher.doFinal(value.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            log.error("Erro ao criptografar valor", e);
            return value;
        }
    }
    
    private String decrypt(String encryptedValue) {
        if (encryptedValue == null || encryptedValue.isEmpty()) {
            return encryptedValue;
        }
        try {
            SecretKeySpec key = new SecretKeySpec(ENCRYPTION_KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedValue));
            return new String(decrypted);
        } catch (Exception e) {
            log.error("Erro ao descriptografar valor", e);
            return encryptedValue;
        }
    }
    
    public Boolean testEmailConnection() {
        // Simular teste de conexão de email
        // Em produção, você implementaria uma conexão real com o servidor SMTP
        String smtpHost = getConfigValue("email.smtpHost", "");
        Integer smtpPort = getIntegerConfig("email.smtpPort", 587);
        String smtpUsername = getConfigValue("email.smtpUsername", "");
        
        if (smtpHost.isEmpty() || smtpUsername.isEmpty()) {
            throw new RuntimeException("Configurações de email incompletas");
        }
        
        log.info("Teste de conexão de email - Host: {}, Port: {}, User: {}", smtpHost, smtpPort, smtpUsername);
        return true;
    }
    
    /**
     * Verifica se o sistema está em modo de manutenção
     */
    public Boolean isMaintenanceMode() {
        return getBooleanConfig("system.maintenanceMode", false);
    }
    
    /**
     * Verifica se o sistema está em modo debug
     */
    public Boolean isDebugMode() {
        return getBooleanConfig("system.debugMode", false);
    }
    
    /**
     * Verifica se o backup automático está habilitado
     */
    public Boolean isAutoBackupEnabled() {
        return getBooleanConfig("backup.autoBackup", true);
    }
    
    /**
     * Obtém a frequência do backup
     */
    public String getBackupFrequency() {
        return getConfigValue("backup.frequency", "daily");
    }
    
    /**
     * Obtém os dias de retenção do backup
     */
    public Integer getBackupRetentionDays() {
        return getIntegerConfig("backup.retention", 30);
    }
}

