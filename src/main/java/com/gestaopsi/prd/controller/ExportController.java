package com.gestaopsi.prd.controller;

import com.gestaopsi.prd.service.ExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/export")
@RequiredArgsConstructor
@Tag(name = "Exportação", description = "Endpoints para exportação de dados")
public class ExportController {

    private final ExportService exportService;

    @PostMapping("/pdf")
    @Operation(summary = "Exportar para PDF", description = "Exporta dados genéricos para PDF")
    public ResponseEntity<byte[]> exportToPDF(@RequestBody Map<String, Object> request) {
        String title = (String) request.get("title");
        @SuppressWarnings("unchecked")
        List<String> headers = (List<String>) request.get("headers");
        @SuppressWarnings("unchecked")
        List<List<String>> rows = (List<List<String>>) request.get("rows");

        byte[] pdfBytes = exportService.exportToPDF(title, headers, rows);

        String filename = sanitizeFilename(title) + "_" + 
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf";

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
            .contentType(MediaType.APPLICATION_PDF)
            .body(pdfBytes);
    }

    @PostMapping("/excel")
    @Operation(summary = "Exportar para Excel", description = "Exporta dados genéricos para Excel")
    public ResponseEntity<byte[]> exportToExcel(@RequestBody Map<String, Object> request) {
        String sheetName = (String) request.get("sheetName");
        @SuppressWarnings("unchecked")
        List<String> headers = (List<String>) request.get("headers");
        @SuppressWarnings("unchecked")
        List<List<String>> rows = (List<List<String>>) request.get("rows");

        byte[] excelBytes = exportService.exportToExcel(sheetName, headers, rows);

        String filename = sanitizeFilename(sheetName) + "_" + 
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
            .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
            .body(excelBytes);
    }

    @PostMapping("/sessoes/pdf")
    @Operation(summary = "Exportar sessões para PDF", description = "Exporta relatório de sessões para PDF")
    public ResponseEntity<byte[]> exportSessoesToPDF(@RequestBody List<Map<String, Object>> sessoes) {
        byte[] pdfBytes = exportService.exportSessoesToPDF(sessoes);

        String filename = "relatorio_sessoes_" + 
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf";

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
            .contentType(MediaType.APPLICATION_PDF)
            .body(pdfBytes);
    }

    @PostMapping("/pagamentos/excel")
    @Operation(summary = "Exportar pagamentos para Excel", description = "Exporta relatório de pagamentos para Excel")
    public ResponseEntity<byte[]> exportPagamentosToExcel(@RequestBody List<Map<String, Object>> pagamentos) {
        byte[] excelBytes = exportService.exportPagamentosToExcel(pagamentos);

        String filename = "relatorio_pagamentos_" + 
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
            .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
            .body(excelBytes);
    }

    private String sanitizeFilename(String filename) {
        return filename.replaceAll("[^a-zA-Z0-9-_]", "_");
    }
}


