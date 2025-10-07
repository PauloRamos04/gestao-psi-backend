package com.gestaopsi.prd.controller;

import com.gestaopsi.prd.service.BackupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/backups")
@Tag(name = "Backups", description = "Gerenciamento de backups do banco de dados")
@RequiredArgsConstructor
public class BackupController {

    private final BackupService backupService;

    @PostMapping("/criar")
    @Operation(summary = "Criar backup manual do banco de dados")
    public ResponseEntity<Map<String, String>> criarBackup() {
        try {
            String backupFile = backupService.criarBackup();
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("arquivo", backupFile);
            response.put("mensagem", "Backup criado com sucesso");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("mensagem", "Erro ao criar backup: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/listar")
    @Operation(summary = "Listar todos os backups disponíveis")
    public ResponseEntity<List<Map<String, Object>>> listarBackups() {
        try {
            File[] backups = backupService.listarBackups();
            List<Map<String, Object>> response = Arrays.stream(backups)
                .map(file -> {
                    Map<String, Object> info = new HashMap<>();
                    info.put("nome", file.getName());
                    info.put("tamanho", file.length());
                    info.put("dataModificacao", file.lastModified());
                    info.put("caminho", file.getAbsolutePath());
                    return info;
                })
                .collect(Collectors.toList());
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/download/{nomeArquivo}")
    @Operation(summary = "Fazer download de um backup específico")
    public ResponseEntity<Resource> downloadBackup(@PathVariable String nomeArquivo) {
        try {
            File[] backups = backupService.listarBackups();
            File backup = Arrays.stream(backups)
                .filter(f -> f.getName().equals(nomeArquivo))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Backup não encontrado"));

            Resource resource = new FileSystemResource(backup);

            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + backup.getName() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(backup.length())
                .body(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/restaurar/{nomeArquivo}")
    @Operation(summary = "Restaurar banco de dados a partir de um backup")
    public ResponseEntity<Map<String, String>> restaurarBackup(@PathVariable String nomeArquivo) {
        try {
            File[] backups = backupService.listarBackups();
            File backup = Arrays.stream(backups)
                .filter(f -> f.getName().equals(nomeArquivo))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Backup não encontrado"));

            backupService.restaurarBackup(backup.getAbsolutePath());

            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("mensagem", "Banco restaurado com sucesso");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("mensagem", "Erro ao restaurar: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}

