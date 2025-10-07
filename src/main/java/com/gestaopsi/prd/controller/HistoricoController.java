package com.gestaopsi.prd.controller;

import com.gestaopsi.prd.service.HistoricoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/historico")
@RequiredArgsConstructor
@Tag(name = "Histórico")
public class HistoricoController {

    private final HistoricoService historicoService;

    @GetMapping("/dashboard")
    @Operation(summary = "Dados do dashboard histórico")
    public ResponseEntity<Map<String, Object>> getDashboard(
            @RequestParam Long clinicaId,
            @RequestParam Long psicologId,
            @RequestParam String inicio,
            @RequestParam String fim) {
        
        LocalDate dataInicio = LocalDate.parse(inicio);
        LocalDate dataFim = LocalDate.parse(fim);
        
        Map<String, Object> dashboard = historicoService.getDashboardData(
            clinicaId, psicologId, dataInicio, dataFim);
        
        return ResponseEntity.ok(dashboard);
    }

    @GetMapping("/timeline")
    @Operation(summary = "Timeline mensal do histórico")
    public ResponseEntity<?> getTimeline(
            @RequestParam Long clinicaId,
            @RequestParam Long psicologId,
            @RequestParam Integer months) {
        
        var timeline = historicoService.getTimelineData(clinicaId, psicologId, months);
        return ResponseEntity.ok(timeline);
    }

    @GetMapping("/salas")
    @Operation(summary = "Uso de salas no período")
    public ResponseEntity<?> getSalasUsage(
            @RequestParam Long clinicaId,
            @RequestParam Long psicologId,
            @RequestParam String inicio,
            @RequestParam String fim) {
        
        LocalDate dataInicio = LocalDate.parse(inicio);
        LocalDate dataFim = LocalDate.parse(fim);
        
        var usage = historicoService.getSalasUsage(clinicaId, psicologId, dataInicio, dataFim);
        return ResponseEntity.ok(usage);
    }

    @GetMapping("/pacientes/evolucao")
    @Operation(summary = "Evolução de pacientes")
    public ResponseEntity<?> getPacientesEvolucao(
            @RequestParam Long clinicaId,
            @RequestParam Long psicologId,
            @RequestParam Integer months) {
        
        var evolucao = historicoService.getPacientesEvolucao(clinicaId, psicologId, months);
        return ResponseEntity.ok(evolucao);
    }

    @GetMapping("/financeiro/evolucao")
    @Operation(summary = "Evolução financeira")
    public ResponseEntity<?> getFinanceiroEvolucao(
            @RequestParam Long clinicaId,
            @RequestParam Long psicologId,
            @RequestParam Integer months) {
        
        var evolucao = historicoService.getFinanceiroEvolucao(clinicaId, psicologId, months);
        return ResponseEntity.ok(evolucao);
    }
}

