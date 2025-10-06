package com.gestaopsi.prd.controller;

import com.gestaopsi.prd.dto.RelatorioFinanceiroDTO;
import com.gestaopsi.prd.dto.RelatorioPacientesDTO;
import com.gestaopsi.prd.dto.RelatorioSessoesDTO;
import com.gestaopsi.prd.service.RelatorioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/relatorios")
@Tag(name = "Relatórios", description = "Geração de relatórios gerenciais")
@RequiredArgsConstructor
public class RelatoriosController {

    private final RelatorioService relatorioService;

    @GetMapping("/sessoes")
    @Operation(summary = "Relatório de sessões")
    public ResponseEntity<RelatorioSessoesDTO> relatorioSessoes(
            @RequestParam Long clinicaId,
            @RequestParam Long psicologId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return ResponseEntity.ok(
            relatorioService.gerarRelatorioSessoes(clinicaId, psicologId, inicio, fim)
        );
    }

    @GetMapping("/pacientes")
    @Operation(summary = "Relatório de pacientes")
    public ResponseEntity<RelatorioPacientesDTO> relatorioPacientes(
            @RequestParam Long clinicaId,
            @RequestParam Long psicologId) {
        return ResponseEntity.ok(
            relatorioService.gerarRelatorioPacientes(clinicaId, psicologId)
        );
    }

    @GetMapping("/financeiro")
    @Operation(summary = "Relatório financeiro")
    public ResponseEntity<RelatorioFinanceiroDTO> relatorioFinanceiro(
            @RequestParam Long clinicaId,
            @RequestParam Long psicologId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return ResponseEntity.ok(
            relatorioService.gerarRelatorioFinanceiro(clinicaId, psicologId, inicio, fim)
        );
    }
}

