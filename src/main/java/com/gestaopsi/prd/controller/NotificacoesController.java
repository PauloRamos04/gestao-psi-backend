package com.gestaopsi.prd.controller;

import com.gestaopsi.prd.entity.Notificacao;
import com.gestaopsi.prd.service.NotificacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notificacoes")
@Tag(name = "Notificações", description = "Sistema de notificações")
@RequiredArgsConstructor
public class NotificacoesController {

    private final NotificacaoService notificacaoService;

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Listar todas notificações do usuário")
    public ResponseEntity<List<Notificacao>> listarTodas(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(notificacaoService.listarTodas(usuarioId));
    }

    @GetMapping("/usuario/{usuarioId}/nao-lidas")
    @Operation(summary = "Listar notificações não lidas")
    public ResponseEntity<List<Notificacao>> listarNaoLidas(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(notificacaoService.listarNaoLidas(usuarioId));
    }

    @GetMapping("/usuario/{usuarioId}/contador")
    @Operation(summary = "Contar notificações não lidas")
    public ResponseEntity<Long> contarNaoLidas(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(notificacaoService.contarNaoLidas(usuarioId));
    }

    @PostMapping("/{id}/marcar-lida")
    @Operation(summary = "Marcar notificação como lida")
    public ResponseEntity<Void> marcarComoLida(@PathVariable Long id) {
        boolean marcado = notificacaoService.marcarComoLida(id);
        return marcado ? ResponseEntity.ok().build() 
                      : ResponseEntity.notFound().build();
    }

    @PostMapping("/usuario/{usuarioId}/marcar-todas-lidas")
    @Operation(summary = "Marcar todas como lidas")
    public ResponseEntity<Void> marcarTodasComoLidas(@PathVariable Long usuarioId) {
        notificacaoService.marcarTodasComoLidas(usuarioId);
        return ResponseEntity.ok().build();
    }
}

