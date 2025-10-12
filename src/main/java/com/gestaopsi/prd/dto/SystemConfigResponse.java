package com.gestaopsi.prd.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemConfigResponse {
    // Configurações de Sistema
    private String systemName;
    private String systemVersion;
    private Boolean maintenanceMode;
    private Boolean debugMode;
    
    // Configurações de Email
    private String smtpHost;
    private Integer smtpPort;
    private String smtpUsername;
    private String smtpPassword;
    private String smtpFromEmail;
    
    // Configurações de Backup
    private Boolean autoBackup;
    private String backupFrequency;
    private Integer backupRetention;
    
    // Configurações de Segurança
    private Integer sessionTimeout;
    private Integer maxLoginAttempts;
    private PasswordPolicyResponse passwordPolicy;
    
    // Configurações de Notificações
    private Boolean emailNotifications;
    private Boolean smsNotifications;
    private Boolean pushNotifications;
    
    // Configurações de Logs
    private String logLevel;
    private Integer logRetention;
    private Boolean auditLogs;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PasswordPolicyResponse {
        private Integer minLength;
        private Boolean requireUppercase;
        private Boolean requireNumbers;
        private Boolean requireSpecialChars;
    }
}

