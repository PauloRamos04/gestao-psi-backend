package com.gestaopsi.prd.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

@RestController
@RequestMapping("/downloads")
@Tag(name = "Downloads")
public class DownloadsController {

    @PostMapping("/solicitar")
    @Operation(summary = "Solicitar geração de arquivo (mock)")
    public ResponseEntity<String> solicitar(@RequestBody DownloadRequest req) {
        return ResponseEntity.ok("REQUEST_RECEIVED");
    }

    @GetMapping("/arquivo-exemplo")
    @Operation(summary = "Baixar arquivo de exemplo (CSV)")
    public ResponseEntity<byte[]> baixarCsvExemplo() {
        String csv = "id,nome,data\n1,Exemplo," + LocalDate.now();
        byte[] bytes = csv.getBytes(StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=exemplo.csv")
                .contentType(MediaType.TEXT_PLAIN)
                .body(bytes);
    }

    @Data
    public static class DownloadRequest {
        private String category;
        private String type;
        private String inicio;
        private String fim;
    }
}


