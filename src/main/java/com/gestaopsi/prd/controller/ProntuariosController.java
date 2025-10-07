package com.gestaopsi.prd.controller;

import com.gestaopsi.prd.dto.ProntuarioRequest;
import com.gestaopsi.prd.dto.ProntuarioResponse;
import com.gestaopsi.prd.entity.Prontuario;
import com.gestaopsi.prd.service.ProntuarioService;
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
@RequestMapping("/prontuarios")
@Tag(name = "Prontuários", description = "Gerenciamento de prontuários eletrônicos")
@RequiredArgsConstructor
public class ProntuariosController {

    private final ProntuarioService prontuarioService;

    @GetMapping("/paciente/{pacienteId}")
    @Operation(summary = "Listar prontuários por paciente")
    public ResponseEntity<List<ProntuarioResponse>> listarPorPaciente(@PathVariable Long pacienteId) {
        List<ProntuarioResponse> response = prontuarioService.listarPorPaciente(pacienteId)
            .stream()
            .map(ProntuarioResponse::fromEntity)
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/paciente/{pacienteId}/paginado")
    @Operation(summary = "Listar prontuários por paciente com paginação")
    public ResponseEntity<Page<Prontuario>> listarPorPacientePaginado(
            @PathVariable Long pacienteId,
            Pageable pageable) {
        return ResponseEntity.ok(
            prontuarioService.listarPorPacientePaginado(pacienteId, pageable)
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar prontuário por ID")
    public ResponseEntity<ProntuarioResponse> buscarPorId(@PathVariable Long id) {
        return prontuarioService.buscarPorId(id)
                .map(ProntuarioResponse::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Criar novo prontuário")
    public ResponseEntity<ProntuarioResponse> criar(@Valid @RequestBody ProntuarioRequest request) {
        Prontuario prontuario = prontuarioService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ProntuarioResponse.fromEntity(prontuario));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar prontuário")
    public ResponseEntity<ProntuarioResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProntuarioRequest request) {
        return prontuarioService.atualizar(id, request)
                .map(ProntuarioResponse::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar prontuário (soft delete)")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        boolean deletado = prontuarioService.deletar(id);
        return deletado ? ResponseEntity.noContent().build() 
                       : ResponseEntity.notFound().build();
    }
}

