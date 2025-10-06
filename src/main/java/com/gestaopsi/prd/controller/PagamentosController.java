package com.gestaopsi.prd.controller;

import com.gestaopsi.prd.dto.PagamentoRequest;
import com.gestaopsi.prd.dto.PagamentoResponse;
import com.gestaopsi.prd.entity.Pagamento;
import com.gestaopsi.prd.service.PagamentoServiceImpl;
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
@RequestMapping("/pagamentos")
@Tag(name = "Pagamentos", description = "Gerenciamento de pagamentos")
@RequiredArgsConstructor
public class PagamentosController {

    private final PagamentoServiceImpl pagamentoService;

    @GetMapping("/periodo")
    @Operation(summary = "Listar pagamentos por período")
    public ResponseEntity<List<PagamentoResponse>> porPeriodo(
            @RequestParam Long clinicaId,
            @RequestParam Long psicologId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        List<PagamentoResponse> pagamentos = pagamentoService.listarPorPeriodo(clinicaId, psicologId, inicio, fim)
                .stream()
                .map(PagamentoResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(pagamentos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pagamento por ID")
    public ResponseEntity<PagamentoResponse> buscarPorId(@PathVariable Long id) {
        return pagamentoService.buscarPorId(id)
                .map(PagamentoResponse::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Criar novo pagamento")
    public ResponseEntity<PagamentoResponse> criar(@Valid @RequestBody PagamentoRequest request) {
        Pagamento pagamento = pagamentoService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(PagamentoResponse.fromEntity(pagamento));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar pagamento")
    public ResponseEntity<PagamentoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody PagamentoRequest request) {
        return pagamentoService.atualizar(id, request)
                .map(PagamentoResponse::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar pagamento")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        boolean deletado = pagamentoService.deletar(id);
        return deletado ? ResponseEntity.noContent().build() 
                       : ResponseEntity.notFound().build();
    }
}
