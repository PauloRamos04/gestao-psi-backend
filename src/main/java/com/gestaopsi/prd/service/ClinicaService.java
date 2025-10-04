package com.gestaopsi.prd.service;

import com.gestaopsi.prd.entity.Clinica;
import com.gestaopsi.prd.repository.ClinicaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClinicaService {

    private final ClinicaRepository clinicaRepository;

    public ClinicaService(ClinicaRepository clinicaRepository) {
        this.clinicaRepository = clinicaRepository;
    }

    public Optional<Clinica> findByLoginAtiva(String clinicaLogin) {
        return clinicaRepository.findByClinicaLoginAndStatusTrue(clinicaLogin);
    }
}


