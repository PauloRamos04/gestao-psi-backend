package com.gestaopsi.prd.controller;

import com.gestaopsi.prd.entity.Sala;
import com.gestaopsi.prd.repository.SalaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/salas")
@Tag(name = "Salas")
public class SalasController {

    private final SalaRepository salaRepository;

    public SalasController(SalaRepository salaRepository) {
        this.salaRepository = salaRepository;
    }

    @GetMapping
    @Operation(summary = "Listar salas por clínica")
    public ResponseEntity<List<Sala>> listar(@RequestParam Integer clinicaId) {
        return ResponseEntity.ok(salaRepository.findByClinicaId(clinicaId));
    }
}
