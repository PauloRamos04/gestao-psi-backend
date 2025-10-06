package com.gestaopsi.prd.service;

import com.gestaopsi.prd.dto.SalaRequest;
import com.gestaopsi.prd.entity.Clinica;
import com.gestaopsi.prd.entity.Sala;
import com.gestaopsi.prd.repository.ClinicaRepository;
import com.gestaopsi.prd.repository.SalaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SalaService {

    private final SalaRepository salaRepository;
    private final ClinicaRepository clinicaRepository;

    public List<Sala> listarPorClinica(Long clinicaId) {
        log.info("Listando salas - Clinica: {}", clinicaId);
        return salaRepository.findByClinicaId(clinicaId.intValue());
    }

    public Optional<Sala> buscarPorId(Long id) {
        return salaRepository.findById(id);
    }

    @Transactional
    public Sala criar(SalaRequest request) {
        log.info("Criando nova sala: {}", request.getNome());
        
        Clinica clinica = clinicaRepository.findById(request.getClinicaId())
            .orElseThrow(() -> new IllegalArgumentException("Clínica não encontrada"));
        
        Sala sala = Sala.builder()
            .clinica(clinica)
            .nome(request.getNome())
            .build();
        
        return salaRepository.save(sala);
    }

    @Transactional
    public Optional<Sala> atualizar(Long id, SalaRequest request) {
        log.info("Atualizando sala: {}", id);
        
        return salaRepository.findById(id)
            .map(sala -> {
                sala.setNome(request.getNome());
                return salaRepository.save(sala);
            });
    }

    @Transactional
    public boolean deletar(Long id) {
        log.info("Deletando sala: {}", id);
        return salaRepository.findById(id)
            .map(sala -> {
                salaRepository.delete(sala);
                return true;
            })
            .orElse(false);
    }
}

