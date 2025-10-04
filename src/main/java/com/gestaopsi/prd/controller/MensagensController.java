package com.gestaopsi.prd.controller;

import com.gestaopsi.prd.entity.Mensagem;
import com.gestaopsi.prd.service.MensagemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mensagens")
@Tag(name = "Mensagens")
public class MensagensController {

    private final MensagemService mensagemService;

    public MensagensController(MensagemService mensagemService) {
        this.mensagemService = mensagemService;
    }

    @GetMapping
    @Operation(summary = "Listar mensagens ativas")
    public ResponseEntity<List<Mensagem>> listarAtivas() {
        return ResponseEntity.ok(mensagemService.listarAtivas());
    }
}
