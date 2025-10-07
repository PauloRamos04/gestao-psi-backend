package com.gestaopsi.prd.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class BackupService {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUser;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Value("${backup.directory:./backups}")
    private String backupDirectory;

    @Value("${backup.enabled:true}")
    private boolean backupEnabled;

    @Value("${backup.retention.days:30}")
    private int retentionDays;

    /**
     * Executa backup diário às 3h da manhã
     */
    @Scheduled(cron = "${backup.cron:0 0 3 * * ?}")
    public void executarBackupDiario() {
        if (!backupEnabled) {
            log.info("Backup automático desabilitado");
            return;
        }

        try {
            log.info("Iniciando backup automático do banco de dados...");
            String backupFile = criarBackup();
            log.info("Backup concluído com sucesso: {}", backupFile);
            
            // Limpar backups antigos
            limparBackupsAntigos();
        } catch (Exception e) {
            log.error("Erro ao executar backup automático: {}", e.getMessage(), e);
        }
    }

    /**
     * Cria backup do banco de dados PostgreSQL usando pg_dump
     */
    public String criarBackup() throws IOException, InterruptedException {
        // Criar diretório de backup se não existir
        Path backupPath = Paths.get(backupDirectory);
        if (!Files.exists(backupPath)) {
            Files.createDirectories(backupPath);
        }

        // Extrair nome do banco da URL
        String dbName = extrairNomeBanco(dbUrl);
        String dbHost = extrairHost(dbUrl);
        String dbPort = extrairPorta(dbUrl);

        // Gerar nome do arquivo com timestamp
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = String.format("backup_%s_%s.dump", dbName, timestamp);
        String fullPath = Paths.get(backupDirectory, fileName).toString();

        // Montar comando pg_dump
        ProcessBuilder processBuilder = new ProcessBuilder(
            "pg_dump",
            "-h", dbHost,
            "-p", dbPort,
            "-U", dbUser,
            "-F", "c", // formato custom (comprimido)
            "-b", // incluir blobs
            "-v", // verbose
            "-f", fullPath,
            dbName
        );

        // Definir senha via variável de ambiente
        processBuilder.environment().put("PGPASSWORD", dbPassword);
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();
        boolean finished = process.waitFor(10, TimeUnit.MINUTES);

        if (!finished) {
            process.destroyForcibly();
            throw new IOException("Backup timeout após 10 minutos");
        }

        int exitCode = process.exitValue();
        if (exitCode != 0) {
            throw new IOException("Falha no backup. Exit code: " + exitCode);
        }

        return fullPath;
    }

    /**
     * Remove backups mais antigos que o período de retenção
     */
    private void limparBackupsAntigos() {
        try {
            Path backupPath = Paths.get(backupDirectory);
            if (!Files.exists(backupPath)) return;

            long cutoffTime = System.currentTimeMillis() - 
                TimeUnit.DAYS.toMillis(retentionDays);

            Files.list(backupPath)
                .filter(path -> path.toString().endsWith(".dump"))
                .filter(path -> {
                    try {
                        return Files.getLastModifiedTime(path).toMillis() < cutoffTime;
                    } catch (IOException e) {
                        return false;
                    }
                })
                .forEach(path -> {
                    try {
                        Files.delete(path);
                        log.info("Backup antigo removido: {}", path.getFileName());
                    } catch (IOException e) {
                        log.error("Erro ao remover backup: {}", path, e);
                    }
                });
        } catch (IOException e) {
            log.error("Erro ao limpar backups antigos", e);
        }
    }

    /**
     * Extrai nome do banco da URL JDBC
     */
    private String extrairNomeBanco(String jdbcUrl) {
        // jdbc:postgresql://host:port/database
        String[] parts = jdbcUrl.split("/");
        String dbWithParams = parts[parts.length - 1];
        return dbWithParams.split("\\?")[0];
    }

    /**
     * Extrai host da URL JDBC
     */
    private String extrairHost(String jdbcUrl) {
        // jdbc:postgresql://host:port/database
        String afterProtocol = jdbcUrl.split("//")[1];
        String hostPort = afterProtocol.split("/")[0];
        return hostPort.split(":")[0];
    }

    /**
     * Extrai porta da URL JDBC
     */
    private String extrairPorta(String jdbcUrl) {
        try {
            String afterProtocol = jdbcUrl.split("//")[1];
            String hostPort = afterProtocol.split("/")[0];
            return hostPort.split(":")[1];
        } catch (Exception e) {
            return "5432"; // porta padrão PostgreSQL
        }
    }

    /**
     * Restaura backup do banco de dados
     */
    public void restaurarBackup(String backupFilePath) throws IOException, InterruptedException {
        String dbName = extrairNomeBanco(dbUrl);
        String dbHost = extrairHost(dbUrl);
        String dbPort = extrairPorta(dbUrl);

        ProcessBuilder processBuilder = new ProcessBuilder(
            "pg_restore",
            "-h", dbHost,
            "-p", dbPort,
            "-U", dbUser,
            "-d", dbName,
            "-c", // limpar antes de restaurar
            "-v",
            backupFilePath
        );

        processBuilder.environment().put("PGPASSWORD", dbPassword);
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();
        boolean finished = process.waitFor(10, TimeUnit.MINUTES);

        if (!finished) {
            process.destroyForcibly();
            throw new IOException("Restauração timeout após 10 minutos");
        }

        int exitCode = process.exitValue();
        if (exitCode != 0) {
            throw new IOException("Falha na restauração. Exit code: " + exitCode);
        }
    }

    /**
     * Lista todos os backups disponíveis
     */
    public File[] listarBackups() throws IOException {
        Path backupPath = Paths.get(backupDirectory);
        if (!Files.exists(backupPath)) {
            return new File[0];
        }

        return backupPath.toFile().listFiles((dir, name) -> name.endsWith(".dump"));
    }
}

