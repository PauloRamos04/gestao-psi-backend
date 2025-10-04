package com.gestaopsi.prd.controller;

import com.gestaopsi.prd.entity.Pagamento;
import com.gestaopsi.prd.service.PagamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/faturamento")
@Tag(name = "Faturamento")
public class FaturamentoController {

    private final PagamentoService pagamentoService;

    public FaturamentoController(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    @GetMapping("/periodo")
    @Operation(summary = "Faturamento por período")
    public ResponseEntity<BigDecimal> faturamentoPorPeriodo(
            @RequestParam Integer clinicaId,
            @RequestParam Integer psicologId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        
        List<Pagamento> pagamentos = pagamentoService.listarPorPeriodo(clinicaId, psicologId, inicio, fim);
        BigDecimal total = pagamentos.stream()
                .map(Pagamento::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        return ResponseEntity.ok(total);
    }
}
