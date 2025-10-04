package com.gestaopsi.prd.controller;

import com.gestaopsi.prd.entity.Psicologo;
import com.gestaopsi.prd.service.PsicologoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/psicologos")
@Tag(name = "Psicólogos")
public class PsicologosController {

    private final PsicologoService psicologoService;

    public PsicologosController(PsicologoService psicologoService) {
        this.psicologoService = psicologoService;
    }

    @GetMapping("/{login}")
    @Operation(summary = "Buscar psicólogo por login")
    public ResponseEntity<Psicologo> getByLogin(@PathVariable String login) {
        return psicologoService.findByLogin(login)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}


