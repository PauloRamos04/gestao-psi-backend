package com.gestaopsi.prd.controller;

import com.gestaopsi.prd.service.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@Tag(name = "Upload de Arquivos", description = "Endpoints para upload e download de arquivos")
public class FileUploadController {

    private final FileStorageService fileStorageService;

    @PostMapping("/upload")
    @Operation(summary = "Upload de arquivo", description = "Faz upload de um arquivo genérico")
    public ResponseEntity<Map<String, String>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "directory", defaultValue = "general") String directory) {
        
        String filename = fileStorageService.storeFile(file, directory);
        
        return ResponseEntity.ok(Map.of(
            "filename", filename,
            "message", "Arquivo enviado com sucesso"
        ));
    }

    @PostMapping("/upload/image")
    @Operation(summary = "Upload de imagem", description = "Faz upload de uma imagem com compressão automática")
    public ResponseEntity<Map<String, String>> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "directory", defaultValue = "images") String directory,
            @RequestParam(value = "maxWidth", defaultValue = "1200") int maxWidth) {
        
        String filename = fileStorageService.storeImage(file, directory, maxWidth);
        
        return ResponseEntity.ok(Map.of(
            "filename", filename,
            "message", "Imagem enviada e processada com sucesso"
        ));
    }

    @GetMapping("/download/{directory}/{filename}")
    @Operation(summary = "Download de arquivo", description = "Baixa um arquivo pelo nome")
    public ResponseEntity<byte[]> downloadFile(
            @PathVariable String directory,
            @PathVariable String filename) {
        
        byte[] fileContent = fileStorageService.loadFile(filename, directory);
        
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(fileContent);
    }

    @DeleteMapping("/{directory}/{filename}")
    @Operation(summary = "Deletar arquivo", description = "Remove um arquivo do sistema")
    public ResponseEntity<Map<String, String>> deleteFile(
            @PathVariable String directory,
            @PathVariable String filename) {
        
        fileStorageService.deleteFile(filename, directory);
        
        return ResponseEntity.ok(Map.of(
            "message", "Arquivo deletado com sucesso"
        ));
    }
}


