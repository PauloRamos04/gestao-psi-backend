package com.gestaopsi.prd.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/uploads")
@Tag(name = "Uploads", description = "Upload de arquivos e imagens")
@Slf4j
public class UploadController {

    @Value("${upload.directory:./uploads}")
    private String uploadDirectory;

    @PostMapping("/foto")
    @Operation(summary = "Upload de foto de perfil")
    public ResponseEntity<Map<String, String>> uploadFoto(@RequestParam("file") MultipartFile file) {
        try {
            // Validar tipo de arquivo
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Apenas imagens são permitidas"));
            }

            // Validar tamanho (max 5MB)
            if (file.getSize() > 5 * 1024 * 1024) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Tamanho máximo permitido: 5MB"));
            }

            // Criar diretório se não existir
            Path uploadPath = Paths.get(uploadDirectory, "fotos");
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Gerar nome único
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null && originalFilename.contains(".")
                    ? originalFilename.substring(originalFilename.lastIndexOf("."))
                    : ".jpg";
            String filename = UUID.randomUUID().toString() + extension;

            // Salvar arquivo
            Path filePath = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Retornar URL
            String fileUrl = "/uploads/fotos/" + filename;
            Map<String, String> response = new HashMap<>();
            response.put("url", fileUrl);
            response.put("filename", filename);
            response.put("message", "Upload realizado com sucesso");

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            log.error("Erro ao fazer upload: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erro ao fazer upload: " + e.getMessage()));
        }
    }

    @PostMapping("/documento")
    @Operation(summary = "Upload de documento")
    public ResponseEntity<Map<String, String>> uploadDocumento(@RequestParam("file") MultipartFile file) {
        try {
            // Validar tamanho (max 10MB)
            if (file.getSize() > 10 * 1024 * 1024) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Tamanho máximo permitido: 10MB"));
            }

            // Criar diretório
            Path uploadPath = Paths.get(uploadDirectory, "documentos");
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Gerar nome único
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null && originalFilename.contains(".")
                    ? originalFilename.substring(originalFilename.lastIndexOf("."))
                    : "";
            String filename = UUID.randomUUID().toString() + extension;

            // Salvar
            Path filePath = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            Map<String, String> response = new HashMap<>();
            response.put("url", "/uploads/documentos/" + filename);
            response.put("filename", filename);
            response.put("message", "Upload realizado com sucesso");

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            log.error("Erro ao fazer upload: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erro ao fazer upload: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{tipo}/{filename}")
    @Operation(summary = "Deletar arquivo")
    public ResponseEntity<Map<String, String>> deletarArquivo(
            @PathVariable String tipo,
            @PathVariable String filename) {
        try {
            Path filePath = Paths.get(uploadDirectory, tipo, filename);
            Files.deleteIfExists(filePath);
            return ResponseEntity.ok(Map.of("message", "Arquivo deletado com sucesso"));
        } catch (IOException e) {
            log.error("Erro ao deletar arquivo: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erro ao deletar arquivo"));
        }
    }
}

