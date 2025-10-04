package com.gestaopsi.prd.controller;

import com.gestaopsi.prd.entity.Usuario;
import com.gestaopsi.prd.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuários")
public class UsuariosController {

    private final UsuarioService usuarioService;

    public UsuariosController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    @Operation(summary = "Listar usuários")
    public ResponseEntity<List<Usuario>> listAll() {
        return ResponseEntity.ok(usuarioService.listAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuário por id")
    public ResponseEntity<Usuario> getById(@PathVariable Long id) {
        return usuarioService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/ativar")
    @Operation(summary = "Ativar usuário")
    public ResponseEntity<Void> ativar(@PathVariable Long id) {
        return usuarioService.ativar(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/desativar")
    @Operation(summary = "Desativar usuário")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        return usuarioService.desativar(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}


