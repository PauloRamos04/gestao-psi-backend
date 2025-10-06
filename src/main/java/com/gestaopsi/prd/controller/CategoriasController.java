package com.gestaopsi.prd.controller;

import com.gestaopsi.prd.entity.Categoria;
import com.gestaopsi.prd.repository.CategoriaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
@Tag(name = "Categorias", description = "Categorias de psicólogos")
@RequiredArgsConstructor
public class CategoriasController {

    private final CategoriaRepository categoriaRepository;

    @GetMapping
    @Operation(summary = "Listar todas as categorias")
    public ResponseEntity<List<Categoria>> listAll() {
        return ResponseEntity.ok(categoriaRepository.findAll());
    }
}

