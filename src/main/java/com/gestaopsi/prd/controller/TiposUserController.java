package com.gestaopsi.prd.controller;

import com.gestaopsi.prd.entity.TipoUser;
import com.gestaopsi.prd.repository.TipoUserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tipos-usuario")
@Tag(name = "Tipos de Usuário", description = "Tipos de usuário do sistema")
@RequiredArgsConstructor
public class TiposUserController {

    private final TipoUserRepository tipoUserRepository;

    @GetMapping
    @Operation(summary = "Listar todos os tipos de usuário")
    public ResponseEntity<List<TipoUser>> listAll() {
        return ResponseEntity.ok(tipoUserRepository.findAll());
    }
}

