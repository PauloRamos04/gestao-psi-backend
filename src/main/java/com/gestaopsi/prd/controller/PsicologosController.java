package com.gestaopsi.prd.controller;

import com.gestaopsi.prd.dto.PsicologoRequest;
import com.gestaopsi.prd.dto.PsicologoResponse;
import com.gestaopsi.prd.entity.Psicologo;
import com.gestaopsi.prd.service.PsicologoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/psicologos")
@Tag(name = "Psicólogos")
public class PsicologosController {

    private final PsicologoService psicologoService;

    public PsicologosController(PsicologoService psicologoService) {
        this.psicologoService = psicologoService;
    }

    @GetMapping
    @Operation(summary = "Listar todos os psicólogos")
    public ResponseEntity<List<PsicologoResponse>> listAll() {
        List<PsicologoResponse> psicologos = psicologoService.listAll()
                .stream()
                .map(PsicologoResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(psicologos);
    }

    @GetMapping("/{login}")
    @Operation(summary = "Buscar psicólogo por login")
    public ResponseEntity<Psicologo> getByLogin(@PathVariable String login) {
        return psicologoService.findByLogin(login)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Criar novo psicólogo")
    public ResponseEntity<PsicologoResponse> criar(@Valid @RequestBody PsicologoRequest request) {
        Psicologo psicologo = psicologoService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(PsicologoResponse.fromEntity(psicologo));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar psicólogo")
    public ResponseEntity<PsicologoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody PsicologoRequest request) {
        return psicologoService.atualizar(id, request)
                .map(PsicologoResponse::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar psicólogo")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        return psicologoService.deletar(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}


