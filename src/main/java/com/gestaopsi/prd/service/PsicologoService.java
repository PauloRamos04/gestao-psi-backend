package com.gestaopsi.prd.service;

import com.gestaopsi.prd.entity.Psicologo;
import com.gestaopsi.prd.repository.PsicologoRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PsicologoService {

    private final PsicologoRepository psicologoRepository;

    public PsicologoService(PsicologoRepository psicologoRepository) {
        this.psicologoRepository = psicologoRepository;
    }

    public Optional<Psicologo> findByLogin(String login) {
        return psicologoRepository.findByPsicologLogin(login);
    }
}


