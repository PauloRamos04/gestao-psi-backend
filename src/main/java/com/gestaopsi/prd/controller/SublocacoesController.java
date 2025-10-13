package com.gestaopsi.prd.controller;

import com.gestaopsi.prd.entity.Sublocacao;
import com.gestaopsi.prd.service.SublocacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sublocacoes")
@RequiredArgsConstructor
@Tag(name = "Sublocações")
public class SublocacoesController {

    private final SublocacaoService sublocacaoService;

    @GetMapping
    @Operation(summary = "Listar sublocações por clínica")
    public ResponseEntity<List<Sublocacao>> listar(@RequestParam Long clinicaId) {
        return ResponseEntity.ok(sublocacaoService.listarPorClinica(clinicaId));
    }

    @PostMapping
    @Operation(summary = "Criar sublocação")
    public ResponseEntity<Sublocacao> criar(@RequestParam Long clinicaId, @RequestBody Sublocacao s) {
        return ResponseEntity.ok(sublocacaoService.criar(clinicaId, s));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar sublocação")
    public ResponseEntity<Sublocacao> atualizar(@PathVariable Long id, @RequestBody Sublocacao s) {
        return ResponseEntity.ok(sublocacaoService.atualizar(id, s));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar sublocação")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        sublocacaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}





