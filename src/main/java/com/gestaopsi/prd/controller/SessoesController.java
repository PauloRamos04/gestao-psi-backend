package com.gestaopsi.prd.controller;

import com.gestaopsi.prd.entity.Sessao;
import com.gestaopsi.prd.service.SessaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/sessoes")
@Tag(name = "Sessões")
public class SessoesController {

    private final SessaoService sessaoService;

    public SessoesController(SessaoService sessaoService) {
        this.sessaoService = sessaoService;
    }

    @GetMapping("/periodo")
    @Operation(summary = "Listar sessões por período")
    public ResponseEntity<List<Sessao>> porPeriodo(
            @RequestParam Integer clinicaId,
            @RequestParam Integer psicologId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return ResponseEntity.ok(sessaoService.listarPorPeriodo(clinicaId, psicologId, inicio, fim));
    }

    @GetMapping("/dia")
    @Operation(summary = "Listar sessões por dia")
    public ResponseEntity<List<Sessao>> porDia(
            @RequestParam Integer clinicaId,
            @RequestParam Integer psicologId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        return ResponseEntity.ok(sessaoService.listarPorData(clinicaId, psicologId, data));
    }
}


