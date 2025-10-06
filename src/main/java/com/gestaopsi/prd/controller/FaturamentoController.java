package com.gestaopsi.prd.controller;

import com.gestaopsi.prd.service.PagamentoServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/faturamento")
@Tag(name = "Faturamento", description = "Cálculo de faturamento")
@RequiredArgsConstructor
public class FaturamentoController {

    private final PagamentoServiceImpl pagamentoService;

    @GetMapping("/periodo")
    @Operation(summary = "Calcular faturamento por período")
    public ResponseEntity<Double> calcularFaturamento(
            @RequestParam Long clinicaId,
            @RequestParam Long psicologId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        
        Double faturamento = pagamentoService.calcularFaturamento(
            clinicaId, psicologId, inicio, fim
        );
        
        return ResponseEntity.ok(faturamento);
    }
}
