package com.gestaopsi.prd.service;

import com.gestaopsi.prd.dto.ProntuarioRequest;
import com.gestaopsi.prd.entity.Paciente;
import com.gestaopsi.prd.entity.Prontuario;
import com.gestaopsi.prd.entity.Psicologo;
import com.gestaopsi.prd.entity.Sessao;
import com.gestaopsi.prd.repository.PacienteRepository;
import com.gestaopsi.prd.repository.ProntuarioRepository;
import com.gestaopsi.prd.repository.PsicologoRepository;
import com.gestaopsi.prd.repository.SessaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProntuarioService {

    private final ProntuarioRepository prontuarioRepository;
    private final PacienteRepository pacienteRepository;
    private final PsicologoRepository psicologoRepository;
    private final SessaoRepository sessaoRepository;

    public List<Prontuario> listarPorPaciente(Long pacienteId) {
        log.info("Listando prontuários do paciente: {}", pacienteId);
        return prontuarioRepository.findByPacienteIdAndStatusTrueOrderByDataRegistroDesc(pacienteId);
    }

    public Page<Prontuario> listarPorPacientePaginado(Long pacienteId, Pageable pageable) {
        return prontuarioRepository.findByPacienteIdAndStatusTrueOrderByDataRegistroDesc(
            pacienteId, pageable
        );
    }

    public Optional<Prontuario> buscarPorId(Long id) {
        return prontuarioRepository.findById(id);
    }

    @Transactional
    public Prontuario criar(ProntuarioRequest request) {
        log.info("Criando prontuário - Paciente: {}, Tipo: {}", 
            request.getPacienteId(), request.getTipo());
        
        Paciente paciente = pacienteRepository.findById(request.getPacienteId())
            .orElseThrow(() -> new IllegalArgumentException("Paciente não encontrado"));
        
        Psicologo psicologo = psicologoRepository.findById(request.getPsicologId())
            .orElseThrow(() -> new IllegalArgumentException("Psicólogo não encontrado"));
        
        Sessao sessao = null;
        if (request.getSessaoId() != null) {
            sessao = sessaoRepository.findById(request.getSessaoId())
                .orElseThrow(() -> new IllegalArgumentException("Sessão não encontrada"));
        }
        
        Prontuario prontuario = Prontuario.builder()
            .paciente(paciente)
            .psicologo(psicologo)
            .sessao(sessao)
            .dataRegistro(LocalDateTime.now())
            .tipo(request.getTipo())
            .titulo(request.getTitulo())
            .conteudo(request.getConteudo())
            .queixaPrincipal(request.getQueixaPrincipal())
            .objetivoTerapeutico(request.getObjetivoTerapeutico())
            .historico(request.getHistorico())
            .evolucao(request.getEvolucao())
            .planoTerapeutico(request.getPlanoTerapeutico())
            .privado(request.getPrivado() != null ? request.getPrivado() : true)
            .status(true)
            .build();
        
        return prontuarioRepository.save(prontuario);
    }

    @Transactional
    public Optional<Prontuario> atualizar(Long id, ProntuarioRequest request) {
        log.info("Atualizando prontuário: {}", id);
        
        return prontuarioRepository.findById(id)
            .map(prontuario -> {
                prontuario.setTipo(request.getTipo());
                prontuario.setTitulo(request.getTitulo());
                prontuario.setConteudo(request.getConteudo());
                prontuario.setQueixaPrincipal(request.getQueixaPrincipal());
                prontuario.setObjetivoTerapeutico(request.getObjetivoTerapeutico());
                prontuario.setHistorico(request.getHistorico());
                prontuario.setEvolucao(request.getEvolucao());
                prontuario.setPlanoTerapeutico(request.getPlanoTerapeutico());
                
                if (request.getPrivado() != null) {
                    prontuario.setPrivado(request.getPrivado());
                }
                
                return prontuarioRepository.save(prontuario);
            });
    }

    @Transactional
    public boolean deletar(Long id) {
        log.info("Deletando prontuário: {}", id);
        return prontuarioRepository.findById(id)
            .map(prontuario -> {
                prontuario.setStatus(false);
                prontuarioRepository.save(prontuario);
                return true;
            })
            .orElse(false);
    }
}

