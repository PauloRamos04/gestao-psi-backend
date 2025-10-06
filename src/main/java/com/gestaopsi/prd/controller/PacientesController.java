package com.gestaopsi.prd.controller;

import com.gestaopsi.prd.dto.PacienteRequest;
import com.gestaopsi.prd.dto.PacienteResponse;
import com.gestaopsi.prd.entity.Paciente;
import com.gestaopsi.prd.service.PacienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pacientes")
@Tag(name = "Pacientes", description = "Gerenciamento de pacientes")
@RequiredArgsConstructor
public class PacientesController {

    private final PacienteService pacienteService;

    @GetMapping
    @Operation(summary = "Listar pacientes por clínica e psicólogo com paginação")
    public ResponseEntity<Page<Paciente>> listar(
            @RequestParam Long clinicaId, 
            @RequestParam Long psicologId,
            Pageable pageable) {
        return ResponseEntity.ok(
            pacienteService.listarPorClinicaEPsicologo(clinicaId, psicologId, pageable)
        );
    }

    @GetMapping("/todos")
    @Operation(summary = "Listar todos os pacientes sem paginação")
    public ResponseEntity<List<PacienteResponse>> listarTodos(
            @RequestParam Long clinicaId, 
            @RequestParam Long psicologId) {
        List<PacienteResponse> pacientes = pacienteService.listarTodos(clinicaId, psicologId)
                .stream()
                .map(PacienteResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(pacientes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar paciente por ID")
    public ResponseEntity<PacienteResponse> buscarPorId(@PathVariable Long id) {
        return pacienteService.buscarPorId(id)
                .map(PacienteResponse::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Criar novo paciente")
    public ResponseEntity<PacienteResponse> criar(
            @Valid @RequestBody PacienteRequest request) {
        Paciente paciente = pacienteService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(PacienteResponse.fromEntity(paciente));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar paciente")
    public ResponseEntity<PacienteResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody PacienteRequest request) {
        return pacienteService.atualizar(id, request)
                .map(PacienteResponse::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar paciente (soft delete)")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        boolean deletado = pacienteService.deletar(id);
        return deletado ? ResponseEntity.noContent().build() 
                       : ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/ativar")
    @Operation(summary = "Ativar paciente")
    public ResponseEntity<Void> ativar(@PathVariable Long id) {
        boolean ativado = pacienteService.ativar(id);
        return ativado ? ResponseEntity.ok().build() 
                      : ResponseEntity.notFound().build();
    }
}
