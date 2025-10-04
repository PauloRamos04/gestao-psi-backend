package com.gestaopsi.prd.controller;

import com.gestaopsi.prd.entity.Paciente;
import com.gestaopsi.prd.repository.PacienteRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pacientes")
@Tag(name = "Pacientes")
public class PacientesController {

    private final PacienteRepository pacienteRepository;

    public PacientesController(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    @GetMapping
    @Operation(summary = "Listar pacientes por clínica e psicólogo")
    public ResponseEntity<List<Paciente>> listar(@RequestParam Integer clinicaId, @RequestParam Integer psicologId) {
        return ResponseEntity.ok(pacienteRepository.findByClinicaIdAndPsicologIdAndStatusTrue(clinicaId, psicologId));
    }
}


