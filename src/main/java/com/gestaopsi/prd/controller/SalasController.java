package com.gestaopsi.prd.controller;

import com.gestaopsi.prd.dto.SalaRequest;
import com.gestaopsi.prd.dto.SalaResponse;
import com.gestaopsi.prd.entity.Sala;
import com.gestaopsi.prd.service.SalaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/salas")
@Tag(name = "Salas", description = "Gerenciamento de salas")
@RequiredArgsConstructor
public class SalasController {

    private final SalaService salaService;

    @GetMapping
    @Operation(summary = "Listar salas por clínica")
    public ResponseEntity<List<SalaResponse>> listar(@RequestParam Long clinicaId) {
        List<SalaResponse> salas = salaService.listarPorClinica(clinicaId)
                .stream()
                .map(SalaResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(salas);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar sala por ID")
    public ResponseEntity<SalaResponse> buscarPorId(@PathVariable Long id) {
        return salaService.buscarPorId(id)
                .map(SalaResponse::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Criar nova sala")
    public ResponseEntity<SalaResponse> criar(@Valid @RequestBody SalaRequest request) {
        Sala sala = salaService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(SalaResponse.fromEntity(sala));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar sala")
    public ResponseEntity<SalaResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody SalaRequest request) {
        return salaService.atualizar(id, request)
                .map(SalaResponse::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar sala")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            boolean deletado = salaService.deletar(id);
            return deletado ? ResponseEntity.noContent().build() 
                           : ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping("/ativas")
    @Operation(summary = "Listar salas ativas de uma clínica")
    public ResponseEntity<List<SalaResponse>> listarSalasAtivas(@RequestParam Long clinicaId) {
        List<SalaResponse> salas = salaService.listarSalasAtivas(clinicaId)
                .stream()
                .map(SalaResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(salas);
    }

    @GetMapping("/disponiveis")
    @Operation(summary = "Listar salas disponíveis em determinado horário")
    public ResponseEntity<List<SalaResponse>> listarSalasDisponiveis(
            @RequestParam Long clinicaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime hora) {
        List<SalaResponse> salas = salaService.listarSalasDisponiveis(clinicaId, data, hora)
                .stream()
                .map(SalaResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(salas);
    }

    @GetMapping("/disponiveis-psicologo")
    @Operation(summary = "Listar salas disponíveis para um psicólogo específico")
    public ResponseEntity<List<SalaResponse>> listarSalasDisponiveisPorPsicologo(
            @RequestParam Long clinicaId,
            @RequestParam Long psicologoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime hora) {
        List<SalaResponse> salas = salaService.listarSalasDisponiveisPorPsicologo(
                clinicaId, psicologoId, data, hora)
                .stream()
                .map(SalaResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(salas);
    }

    @GetMapping("/psicologo/{psicologoId}")
    @Operation(summary = "Listar salas de um psicólogo (preferenciais/responsável)")
    public ResponseEntity<List<SalaResponse>> listarSalasPorPsicologo(@PathVariable Long psicologoId) {
        List<SalaResponse> salas = salaService.listarSalasPorPsicologo(psicologoId)
                .stream()
                .map(SalaResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(salas);
    }

    @GetMapping("/psicologo/{psicologoId}/preferencial")
    @Operation(summary = "Buscar sala preferencial/padrão do psicólogo")
    public ResponseEntity<SalaResponse> buscarSalaPreferencial(@PathVariable Long psicologoId) {
        return salaService.buscarSalaPreferencialPsicologo(psicologoId)
                .map(SalaResponse::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/disponibilidade")
    @Operation(summary = "Verificar se sala está disponível em horário específico")
    public ResponseEntity<Boolean> verificarDisponibilidade(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime hora) {
        boolean disponivel = salaService.verificarDisponibilidade(id, data, hora);
        return ResponseEntity.ok(disponivel);
    }

    @GetMapping("/{id}/estatisticas")
    @Operation(summary = "Obter estatísticas de uso da sala")
    public ResponseEntity<SalaService.SalaEstatisticas> getEstatisticas(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        SalaService.SalaEstatisticas stats = salaService.getEstatisticas(id, dataInicio, dataFim);
        return ResponseEntity.ok(stats);
    }
}
