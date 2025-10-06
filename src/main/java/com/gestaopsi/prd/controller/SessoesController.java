package com.gestaopsi.prd.controller;

import com.gestaopsi.prd.dto.SessaoRequest;
import com.gestaopsi.prd.dto.SessaoResponse;
import com.gestaopsi.prd.entity.Sessao;
import com.gestaopsi.prd.service.SessaoServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sessoes")
@Tag(name = "Sessões", description = "Gerenciamento de sessões")
@RequiredArgsConstructor
public class SessoesController {

    private final SessaoServiceImpl sessaoService;

    @GetMapping("/periodo")
    @Operation(summary = "Listar sessões por período")
    public ResponseEntity<List<SessaoResponse>> porPeriodo(
            @RequestParam Long clinicaId,
            @RequestParam Long psicologId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        List<SessaoResponse> sessoes = sessaoService.listarPorPeriodo(clinicaId, psicologId, inicio, fim)
                .stream()
                .map(SessaoResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(sessoes);
    }

    @GetMapping("/dia")
    @Operation(summary = "Listar sessões por dia")
    public ResponseEntity<List<SessaoResponse>> porDia(
            @RequestParam Long clinicaId,
            @RequestParam Long psicologId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        List<SessaoResponse> sessoes = sessaoService.listarPorData(clinicaId, psicologId, data)
                .stream()
                .map(SessaoResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(sessoes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar sessão por ID")
    public ResponseEntity<SessaoResponse> buscarPorId(@PathVariable Long id) {
        return sessaoService.buscarPorId(id)
                .map(SessaoResponse::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Criar nova sessão")
    public ResponseEntity<SessaoResponse> criar(@Valid @RequestBody SessaoRequest request) {
        Sessao sessao = sessaoService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(SessaoResponse.fromEntity(sessao));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar sessão")
    public ResponseEntity<SessaoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody SessaoRequest request) {
        return sessaoService.atualizar(id, request)
                .map(SessaoResponse::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar sessão")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        boolean cancelado = sessaoService.cancelar(id);
        return cancelado ? ResponseEntity.ok().build() 
                        : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar sessão")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        boolean deletado = sessaoService.deletar(id);
        return deletado ? ResponseEntity.noContent().build() 
                       : ResponseEntity.notFound().build();
    }
}
