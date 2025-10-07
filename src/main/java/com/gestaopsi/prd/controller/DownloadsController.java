package com.gestaopsi.prd.controller;

import com.gestaopsi.prd.entity.DownloadRequest;
import com.gestaopsi.prd.repository.DownloadRequestRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/downloads")
@Tag(name = "Downloads")
@RequiredArgsConstructor
public class DownloadsController {

    private final DownloadRequestRepository downloadRequestRepository;

    @PostMapping("/solicitar")
    @Operation(summary = "Solicitar geração de arquivo")
    public ResponseEntity<Map<String, Object>> solicitar(@RequestBody DownloadSolicitacaoRequest req) {
        DownloadRequest downloadRequest = DownloadRequest.builder()
                .category(req.getCategory())
                .type(req.getType())
                .inicio(req.getInicio())
                .fim(req.getFim())
                .status("pending")
                .userId(1L) // TODO: pegar do usuário logado
                .build();
        
        downloadRequest = downloadRequestRepository.save(downloadRequest);
        
        Map<String, Object> response = new HashMap<>();
        response.put("id", downloadRequest.getId());
        response.put("status", downloadRequest.getStatus());
        response.put("message", "Solicitação criada. Você será notificado quando o download estiver pronto.");
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/listar")
    @Operation(summary = "Listar downloads disponíveis")
    public ResponseEntity<List<Map<String, Object>>> listarDownloads() {
        List<DownloadRequest> downloads = downloadRequestRepository.findByStatus("completed");
        
        List<Map<String, Object>> response = downloads.stream().map(d -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", d.getId());
            map.put("name", d.getFileName());
            map.put("size", d.getFileSize());
            map.put("type", d.getType());
            map.put("category", d.getCategory());
            map.put("status", "available");
            map.put("createdAt", d.getCreatedAt());
            return map;
        }).collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/requisicoes")
    @Operation(summary = "Listar requisições de download")
    public ResponseEntity<List<Map<String, Object>>> listarRequisicoes() {
        List<DownloadRequest> requests = downloadRequestRepository.findAll();
        
        List<Map<String, Object>> response = requests.stream().map(r -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", r.getId());
            map.put("category", r.getCategory());
            map.put("type", r.getType());
            map.put("status", r.getStatus());
            map.put("createdAt", r.getCreatedAt());
            map.put("completedAt", r.getCompletedAt());
            return map;
        }).collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/download/{id}")
    @Operation(summary = "Fazer download de arquivo")
    public ResponseEntity<Resource> baixarArquivo(@PathVariable Long id) {
        DownloadRequest download = downloadRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Download não encontrado"));
        
        if (download.getFilePath() != null && new File(download.getFilePath()).exists()) {
            Resource resource = new FileSystemResource(download.getFilePath());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + download.getFileName() + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        }
        
        // Fallback para arquivo exemplo
        String content = "Nome,Email,Telefone\nJoão Silva,joao@email.com,(11) 98765-4321\n";
        ByteArrayResource resource = new ByteArrayResource(content.getBytes());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=exemplo.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(resource);
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
    public static class DownloadSolicitacaoRequest {
        private String category;
        private String type;
        private String inicio;
        private String fim;
    }
}
