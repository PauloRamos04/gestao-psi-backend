package com.gestaopsi.prd.service;

import com.gestaopsi.prd.dto.PacienteRequest;
import com.gestaopsi.prd.entity.Clinica;
import com.gestaopsi.prd.entity.Paciente;
import com.gestaopsi.prd.entity.Psicologo;
import com.gestaopsi.prd.repository.ClinicaRepository;
import com.gestaopsi.prd.repository.PacienteRepository;
import com.gestaopsi.prd.repository.PsicologoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PacienteService {

    private final PacienteRepository pacienteRepository;
    private final ClinicaRepository clinicaRepository;
    private final PsicologoRepository psicologoRepository;

    public Page<Paciente> listarPorClinicaEPsicologo(
            Long clinicaId, 
            Long psicologId, 
            Pageable pageable) {
        
        log.info("Listando pacientes - Clinica: {}, Psicólogo: {}", clinicaId, psicologId);
        return pacienteRepository.findByClinicaIdAndPsicologIdAndStatusTrue(
            clinicaId.intValue(), psicologId.intValue(), pageable
        );
    }

    public List<Paciente> listarTodos(Long clinicaId, Long psicologId) {
        return pacienteRepository.findByClinicaIdAndPsicologIdAndStatusTrue(
            clinicaId.intValue(), psicologId.intValue()
        );
    }

    public Optional<Paciente> buscarPorId(Long id) {
        return pacienteRepository.findById(id);
    }

    @Transactional
    public Paciente criar(PacienteRequest request) {
        log.info("Criando novo paciente: {}", request.getNome());
        
        Clinica clinica = clinicaRepository.findById(request.getClinicaId())
            .orElseThrow(() -> new IllegalArgumentException("Clínica não encontrada"));
        
        Psicologo psicologo = psicologoRepository.findById(request.getPsicologId())
            .orElseThrow(() -> new IllegalArgumentException("Psicólogo não encontrado"));
        
        Paciente paciente = Paciente.builder()
            .clinica(clinica)
            .psicologo(psicologo)
            .nome(request.getNome())
            .status(true)
            .build();
        
        return pacienteRepository.save(paciente);
    }

    @Transactional
    public Optional<Paciente> atualizar(Long id, PacienteRequest request) {
        log.info("Atualizando paciente: {}", id);
        
        return pacienteRepository.findById(id)
            .map(paciente -> {
                paciente.setNome(request.getNome());
                return pacienteRepository.save(paciente);
            });
    }

    @Transactional
    public boolean deletar(Long id) {
        log.info("Deletando paciente: {}", id);
        return pacienteRepository.findById(id)
            .map(paciente -> {
                paciente.setStatus(false);
                pacienteRepository.save(paciente);
                return true;
            })
            .orElse(false);
    }

    @Transactional
    public boolean ativar(Long id) {
        return pacienteRepository.findById(id)
            .map(paciente -> {
                paciente.setStatus(true);
                pacienteRepository.save(paciente);
                return true;
            })
            .orElse(false);
    }
}

