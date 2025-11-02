package com.gestaopsi.prd.controller;

import com.gestaopsi.prd.entity.LogAuditoria;
import com.gestaopsi.prd.service.LogAuditoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/logs")
@Tag(name = "Logs de Auditoria", description = "Visualização de logs e auditoria do sistema")
@RequiredArgsConstructor
public class LogsController {

    private final LogAuditoriaService logService;

    @GetMapping
    @Operation(summary = "Listar todos os logs com paginação")
    public ResponseEntity<Page<LogAuditoria>> listarTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false, defaultValue = "DESC") String order
    ) {
        Pageable pageable;
        if (sort != null && !sort.isEmpty()) {
            // Ordenação customizada
            org.springframework.data.domain.Sort.Direction direction = 
                "ASC".equalsIgnoreCase(order) ? 
                    org.springframework.data.domain.Sort.Direction.ASC : 
                    org.springframework.data.domain.Sort.Direction.DESC;
            pageable = PageRequest.of(page, size, org.springframework.data.domain.Sort.by(direction, sort));
        } else {
            // Por padrão, ordena por dataHora DESC
            pageable = PageRequest.of(page, size, org.springframework.data.domain.Sort.by(
                org.springframework.data.domain.Sort.Direction.DESC, "dataHora"));
        }
        return ResponseEntity.ok(logService.buscarTodos(pageable));
    }

    @GetMapping("/filtrar")
    @Operation(summary = "Buscar logs com filtros")
    public ResponseEntity<Page<LogAuditoria>> buscarComFiltros(
            @RequestParam(required = false) Long usuarioId,
            @RequestParam(required = false) String entidade,
            @RequestParam(required = false) String acao,
            @RequestParam(required = false) String modulo,
            @RequestParam(required = false) Long clinicaId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(logService.buscarComFiltros(
            usuarioId, entidade, acao, modulo, clinicaId, inicio, fim, pageable
        ));
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Buscar logs de um usuário")
    public ResponseEntity<Page<LogAuditoria>> buscarPorUsuario(
            @PathVariable Long usuarioId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(logService.buscarPorUsuario(usuarioId, pageable));
    }

    @GetMapping("/entidade/{entidade}")
    @Operation(summary = "Buscar logs de uma entidade")
    public ResponseEntity<Page<LogAuditoria>> buscarPorEntidade(
            @PathVariable String entidade,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(logService.buscarPorEntidade(entidade, pageable));
    }

    @GetMapping("/erros")
    @Operation(summary = "Buscar apenas logs de erro")
    public ResponseEntity<Page<LogAuditoria>> buscarErros(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(logService.buscarErros(pageable));
    }

    @GetMapping("/estatisticas")
    @Operation(summary = "Obter estatísticas de logs")
    public ResponseEntity<Map<String, Object>> getEstatisticas(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio
    ) {
        if (inicio == null) {
            inicio = LocalDateTime.now().minusDays(30); // Últimos 30 dias por padrão
        }
        return ResponseEntity.ok(logService.getEstatisticas(inicio));
    }
}

