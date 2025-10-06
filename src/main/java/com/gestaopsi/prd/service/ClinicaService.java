package com.gestaopsi.prd.service;

import com.gestaopsi.prd.dto.ClinicaRequest;
import com.gestaopsi.prd.entity.Clinica;
import com.gestaopsi.prd.repository.ClinicaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClinicaService {

    private final ClinicaRepository clinicaRepository;

    public List<Clinica> listAll() {
        log.info("Listando todas as clínicas");
        return clinicaRepository.findAll();
    }

    public Optional<Clinica> findByLoginAtiva(String clinicaLogin) {
        return clinicaRepository.findByClinicaLoginAndStatusTrue(clinicaLogin);
    }

    @Transactional
    public Clinica criar(ClinicaRequest request) {
        log.info("Criando nova clínica: {}", request.getNome());
        
        Clinica clinica = Clinica.builder()
                .clinicaLogin(request.getClinicaLogin())
                .nome(request.getNome())
                .titulo(request.getTitulo())
                .status(request.getStatus() != null ? request.getStatus() : true)
                .build();
        
        return clinicaRepository.save(clinica);
    }

    @Transactional
    public Optional<Clinica> atualizar(Long id, ClinicaRequest request) {
        log.info("Atualizando clínica: {}", id);
        
        return clinicaRepository.findById(id)
                .map(clinica -> {
                    clinica.setClinicaLogin(request.getClinicaLogin());
                    clinica.setNome(request.getNome());
                    clinica.setTitulo(request.getTitulo());
                    if (request.getStatus() != null) {
                        clinica.setStatus(request.getStatus());
                    }
                    return clinicaRepository.save(clinica);
                });
    }

    @Transactional
    public boolean ativar(Long id) {
        return clinicaRepository.findById(id)
                .map(clinica -> {
                    clinica.setStatus(true);
                    clinicaRepository.save(clinica);
                    return true;
                })
                .orElse(false);
    }

    @Transactional
    public boolean desativar(Long id) {
        return clinicaRepository.findById(id)
                .map(clinica -> {
                    clinica.setStatus(false);
                    clinicaRepository.save(clinica);
                    return true;
                })
                .orElse(false);
    }
}


