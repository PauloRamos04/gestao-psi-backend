package com.gestaopsi.prd.controller;

import com.gestaopsi.prd.entity.InteracaoIndicacao;
import com.gestaopsi.prd.entity.InteracaoSugestao;
import com.gestaopsi.prd.service.InteracoesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/interacoes")
@RequiredArgsConstructor
@Tag(name = "Interações")
public class InteracoesController {

    private final InteracoesService interacoesService;

    @GetMapping("/sugestoes")
    @Operation(summary = "Listar sugestões")
    public ResponseEntity<List<InteracaoSugestao>> listarSugestoes() {
        return ResponseEntity.ok(interacoesService.listarSugestoes());
    }

    @PostMapping("/sugestoes")
    @Operation(summary = "Criar sugestão")
    public ResponseEntity<InteracaoSugestao> criarSugestao(@RequestBody InteracaoSugestao s) {
        return ResponseEntity.ok(interacoesService.criarSugestao(s));
    }

    @GetMapping("/indicacoes")
    @Operation(summary = "Listar indicações")
    public ResponseEntity<List<InteracaoIndicacao>> listarIndicacoes() {
        return ResponseEntity.ok(interacoesService.listarIndicacoes());
    }

    @PostMapping("/indicacoes")
    @Operation(summary = "Criar indicação")
    public ResponseEntity<InteracaoIndicacao> criarIndicacao(@RequestBody InteracaoIndicacao i) {
        return ResponseEntity.ok(interacoesService.criarIndicacao(i));
    }
}


