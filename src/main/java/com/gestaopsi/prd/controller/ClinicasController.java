package com.gestaopsi.prd.controller;

import com.gestaopsi.prd.entity.Clinica;
import com.gestaopsi.prd.service.ClinicaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clinicas")
@Tag(name = "Clínicas")
public class ClinicasController {

    private final ClinicaService clinicaService;

    public ClinicasController(ClinicaService clinicaService) {
        this.clinicaService = clinicaService;
    }

    @GetMapping("/{clinicaLogin}")
    @Operation(summary = "Buscar clínica ativa por login")
    public ResponseEntity<Clinica> getByLogin(@PathVariable String clinicaLogin) {
        return clinicaService.findByLoginAtiva(clinicaLogin)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}


