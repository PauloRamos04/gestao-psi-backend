package com.gestaopsi.prd.service;

import com.gestaopsi.prd.dto.SessaoRequest;
import com.gestaopsi.prd.entity.Clinica;
import com.gestaopsi.prd.entity.Paciente;
import com.gestaopsi.prd.entity.Psicologo;
import com.gestaopsi.prd.entity.Sala;
import com.gestaopsi.prd.entity.Sessao;
import com.gestaopsi.prd.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SessaoServiceImpl {

    private final SessaoRepository sessaoRepository;
    private final ClinicaRepository clinicaRepository;
    private final PsicologoRepository psicologoRepository;
    private final PacienteRepository pacienteRepository;
    private final SalaRepository salaRepository;

    public List<Sessao> listarPorPeriodo(
            Long clinicaId, 
            Long psicologId, 
            LocalDate inicio, 
            LocalDate fim) {
        
        log.info("Listando sessões por período - Clinica: {}, Psicólogo: {}, Período: {} a {}", 
            clinicaId, psicologId, inicio, fim);
        
        return sessaoRepository.findByClinicaIdAndPsicologIdAndDataBetween(
            clinicaId.intValue(), 
            psicologId.intValue(), 
            inicio, 
            fim
        );
    }

    public List<Sessao> listarPorData(Long clinicaId, Long psicologId, LocalDate data) {
        log.info("Listando sessões por data - Clinica: {}, Psicólogo: {}, Data: {}", 
            clinicaId, psicologId, data);
        
        return sessaoRepository.findByClinicaIdAndPsicologIdAndData(
            clinicaId.intValue(), 
            psicologId.intValue(), 
            data
        );
    }

    public Optional<Sessao> buscarPorId(Long id) {
        return sessaoRepository.findById(id);
    }

    @Transactional
    public Sessao criar(SessaoRequest request) {
        log.info("Criando nova sessão - Paciente ID: {}, Data: {}, Hora: {}", 
            request.getPacienteId(), request.getData(), request.getHora());
        
        // Buscar entidades relacionadas
        Clinica clinica = clinicaRepository.findById(request.getClinicaId())
            .orElseThrow(() -> new IllegalArgumentException("Clínica não encontrada"));
        
        Psicologo psicologo = psicologoRepository.findById(request.getPsicologId())
            .orElseThrow(() -> new IllegalArgumentException("Psicólogo não encontrado"));
        
        Paciente paciente = pacienteRepository.findById(request.getPacienteId())
            .orElseThrow(() -> new IllegalArgumentException("Paciente não encontrado"));
        
        Sala sala = null;
        if (request.getSalaId() != null) {
            sala = salaRepository.findById(request.getSalaId())
                .orElseThrow(() -> new IllegalArgumentException("Sala não encontrada"));
        }
        
        // Validar conflitos
        validarConflitos(request.getPsicologId(), request.getSalaId(), 
            request.getPacienteId(), request.getData(), request.getHora(), null);
        
        // Criar sessão
        Sessao sessao = Sessao.builder()
            .clinica(clinica)
            .psicologo(psicologo)
            .paciente(paciente)
            .sala(sala)
            .data(request.getData())
            .hora(request.getHora())
            .status(true)
            .build();
        
        return sessaoRepository.save(sessao);
    }

    @Transactional
    public Optional<Sessao> atualizar(Long id, SessaoRequest request) {
        log.info("Atualizando sessão: {}", id);
        
        return sessaoRepository.findById(id)
            .map(sessao -> {
                // Atualizar campos
                sessao.setData(request.getData());
                sessao.setHora(request.getHora());
                
                if (request.getSalaId() != null) {
                    Sala sala = salaRepository.findById(request.getSalaId())
                        .orElseThrow(() -> new IllegalArgumentException("Sala não encontrada"));
                    sessao.setSala(sala);
                }
                
                // Validar conflitos (excluindo a própria sessão)
                validarConflitos(sessao.getPsicologo().getId(), 
                    sessao.getSala() != null ? sessao.getSala().getId() : null,
                    sessao.getPaciente().getId(), 
                    request.getData(), 
                    request.getHora(),
                    id);
                
                return sessaoRepository.save(sessao);
            });
    }

    @Transactional
    public boolean cancelar(Long id) {
        log.info("Cancelando sessão: {}", id);
        return sessaoRepository.findById(id)
            .map(sessao -> {
                sessao.setStatus(false);
                sessaoRepository.save(sessao);
                return true;
            })
            .orElse(false);
    }

    @Transactional
    public boolean deletar(Long id) {
        log.info("Deletando sessão: {}", id);
        return sessaoRepository.findById(id)
            .map(sessao -> {
                sessaoRepository.delete(sessao);
                return true;
            })
            .orElse(false);
    }

    private void validarConflitos(Long psicologId, Long salaId, Long pacienteId, 
                                   LocalDate data, LocalTime hora, Long sessaoIdExcluir) {
        
        // Validar conflito de psicólogo
        List<Sessao> conflitosPsicologo = sessaoRepository
            .findByPsicologIdAndDataAndHora(psicologId, data, hora);
        
        if (sessaoIdExcluir != null) {
            conflitosPsicologo.removeIf(s -> s.getId().equals(sessaoIdExcluir));
        }
        
        if (!conflitosPsicologo.isEmpty()) {
            throw new IllegalArgumentException("Psicólogo já possui sessão agendada neste horário");
        }
        
        // Validar conflito de sala
        if (salaId != null) {
            List<Sessao> conflitosSala = sessaoRepository
                .findBySalaIdAndDataAndHora(salaId.intValue(), data, hora);
            
            if (sessaoIdExcluir != null) {
                conflitosSala.removeIf(s -> s.getId().equals(sessaoIdExcluir));
            }
            
            if (!conflitosSala.isEmpty()) {
                throw new IllegalArgumentException("Sala já está ocupada neste horário");
            }
        }
        
        // Validar conflito de paciente
        List<Sessao> conflitosPaciente = sessaoRepository
            .findByPacienteIdAndDataAndHora(pacienteId, data, hora);
        
        if (sessaoIdExcluir != null) {
            conflitosPaciente.removeIf(s -> s.getId().equals(sessaoIdExcluir));
        }
        
        if (!conflitosPaciente.isEmpty()) {
            throw new IllegalArgumentException("Paciente já possui sessão agendada neste horário");
        }
    }
}

