package com.gestaopsi.prd.controller;

import com.gestaopsi.prd.dto.ClinicaRequest;
import com.gestaopsi.prd.dto.ClinicaResponse;
import com.gestaopsi.prd.entity.Clinica;
import com.gestaopsi.prd.service.ClinicaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/clinicas")
@Tag(name = "Clínicas")
public class ClinicasController {

    private final ClinicaService clinicaService;

    public ClinicasController(ClinicaService clinicaService) {
        this.clinicaService = clinicaService;
    }

    @GetMapping
    @Operation(summary = "Listar todas as clínicas")
    public ResponseEntity<List<ClinicaResponse>> listAll() {
        List<ClinicaResponse> clinicas = clinicaService.listAll()
                .stream()
                .map(ClinicaResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(clinicas);
    }

    @GetMapping("/{clinicaLogin}")
    @Operation(summary = "Buscar clínica ativa por login")
    public ResponseEntity<Clinica> getByLogin(@PathVariable String clinicaLogin) {
        return clinicaService.findByLoginAtiva(clinicaLogin)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Criar nova clínica")
    public ResponseEntity<ClinicaResponse> criar(@Valid @RequestBody ClinicaRequest request) {
        Clinica clinica = clinicaService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ClinicaResponse.fromEntity(clinica));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar clínica")
    public ResponseEntity<ClinicaResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ClinicaRequest request) {
        return clinicaService.atualizar(id, request)
                .map(ClinicaResponse::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/ativar")
    @Operation(summary = "Ativar clínica")
    public ResponseEntity<Void> ativar(@PathVariable Long id) {
        return clinicaService.ativar(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/desativar")
    @Operation(summary = "Desativar clínica")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        return clinicaService.desativar(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}


