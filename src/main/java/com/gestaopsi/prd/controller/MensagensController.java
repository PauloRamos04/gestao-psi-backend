package com.gestaopsi.prd.controller;

import com.gestaopsi.prd.dto.MensagemRequest;
import com.gestaopsi.prd.entity.Mensagem;
import com.gestaopsi.prd.service.MensagemServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mensagens")
@Tag(name = "Mensagens", description = "Gerenciamento de mensagens")
@RequiredArgsConstructor
public class MensagensController {

    private final MensagemServiceImpl mensagemService;

    @GetMapping
    @Operation(summary = "Listar mensagens ativas")
    public ResponseEntity<List<Mensagem>> listarAtivas() {
        return ResponseEntity.ok(mensagemService.listarAtivas());
    }

    @GetMapping("/todas")
    @Operation(summary = "Listar todas as mensagens")
    public ResponseEntity<List<Mensagem>> listarTodas() {
        return ResponseEntity.ok(mensagemService.listarTodas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar mensagem por ID")
    public ResponseEntity<Mensagem> buscarPorId(@PathVariable Long id) {
        return mensagemService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Criar nova mensagem")
    public ResponseEntity<Mensagem> criar(@Valid @RequestBody MensagemRequest request) {
        Mensagem mensagem = mensagemService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(mensagem);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar mensagem")
    public ResponseEntity<Mensagem> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody MensagemRequest request) {
        return mensagemService.atualizar(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/desativar")
    @Operation(summary = "Desativar mensagem")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        boolean desativado = mensagemService.desativar(id);
        return desativado ? ResponseEntity.ok().build() 
                         : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar mensagem")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        boolean deletado = mensagemService.deletar(id);
        return deletado ? ResponseEntity.noContent().build() 
                       : ResponseEntity.notFound().build();
    }
}
