package com.gestaopsi.prd.controller;

import com.gestaopsi.prd.entity.Pagamento;
import com.gestaopsi.prd.service.PagamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/pagamentos")
@Tag(name = "Pagamentos")
public class PagamentosController {

    private final PagamentoService pagamentoService;

    public PagamentosController(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    @GetMapping("/periodo")
    @Operation(summary = "Listar pagamentos por período")
    public ResponseEntity<List<Pagamento>> porPeriodo(
            @RequestParam Integer clinicaId,
            @RequestParam Integer psicologId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return ResponseEntity.ok(pagamentoService.listarPorPeriodo(clinicaId, psicologId, inicio, fim));
    }
}
