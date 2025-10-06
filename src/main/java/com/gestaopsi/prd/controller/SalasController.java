package com.gestaopsi.prd.controller;

import com.gestaopsi.prd.dto.SalaRequest;
import com.gestaopsi.prd.dto.SalaResponse;
import com.gestaopsi.prd.entity.Sala;
import com.gestaopsi.prd.service.SalaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        boolean deletado = salaService.deletar(id);
        return deletado ? ResponseEntity.noContent().build() 
                       : ResponseEntity.notFound().build();
    }
}
