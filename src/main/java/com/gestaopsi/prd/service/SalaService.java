package com.gestaopsi.prd.service;

import com.gestaopsi.prd.dto.SalaRequest;
import com.gestaopsi.prd.entity.Clinica;
import com.gestaopsi.prd.entity.Psicologo;
import com.gestaopsi.prd.entity.Sala;
import com.gestaopsi.prd.entity.Sessao;
import com.gestaopsi.prd.repository.ClinicaRepository;
import com.gestaopsi.prd.repository.PsicologoRepository;
import com.gestaopsi.prd.repository.SalaRepository;
import com.gestaopsi.prd.repository.SessaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SalaService {

    private final SalaRepository salaRepository;
    private final ClinicaRepository clinicaRepository;
    private final SessaoRepository sessaoRepository;
    private final PsicologoRepository psicologoRepository;

    @Transactional(readOnly = true)
    @Cacheable(value = "salas", key = "#clinicaId")
    public List<Sala> listarPorClinica(Long clinicaId) {
        log.info("Listando salas - Clinica: {} (sem cache)", clinicaId);
        // Usar fetch join para evitar LazyInitializationException
        return salaRepository.findByClinicaId(clinicaId);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "salas", key = "'ativas_' + #clinicaId")
    public List<Sala> listarSalasAtivas(Long clinicaId) {
        log.info("Listando salas ativas - Clinica: {} (sem cache)", clinicaId);
        // Usar fetch join para evitar LazyInitializationException
        return salaRepository.findByClinicaId(clinicaId)
            .stream()
            .filter(Sala::getAtiva)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "salas", key = "'sala_' + #id")
    public Optional<Sala> buscarPorId(Long id) {
        // Usar fetch join para evitar LazyInitializationException
        return salaRepository.findByIdWithRelations(id);
    }

    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "salas", key = "#request.clinicaId"),
        @CacheEvict(value = "salas", key = "'ativas_' + #request.clinicaId")
    })
    public Sala criar(SalaRequest request) {
        log.info("Criando nova sala: {}", request.getNome());
        
        Clinica clinica = clinicaRepository.findById(request.getClinicaId())
            .orElseThrow(() -> new IllegalArgumentException("Clínica não encontrada"));
        
        Psicologo psicologoResponsavel = null;
        if (request.getPsicologoResponsavelId() != null) {
            psicologoResponsavel = psicologoRepository.findById(request.getPsicologoResponsavelId())
                .orElseThrow(() -> new IllegalArgumentException("Psicólogo não encontrado"));
        }
        
        Sala sala = Sala.builder()
            .clinica(clinica)
            .psicologoResponsavel(psicologoResponsavel)
            .nome(request.getNome())
            .numero(request.getNumero())
            .descricao(request.getDescricao())
            .capacidade(request.getCapacidade())
            .ativa(request.getAtiva() != null ? request.getAtiva() : true)
            .cor(request.getCor() != null ? request.getCor() : "#3B82F6")
            .andar(request.getAndar())
            .bloco(request.getBloco())
            .observacoes(request.getObservacoes())
            .exclusiva(request.getExclusiva() != null ? request.getExclusiva() : false)
            .permiteCompartilhamento(request.getPermiteCompartilhamento() != null ? 
                request.getPermiteCompartilhamento() : true)
            .build();
        
        return salaRepository.save(sala);
    }

    @Transactional
    @CacheEvict(value = "salas", allEntries = true)
    public Optional<Sala> atualizar(Long id, SalaRequest request) {
        log.info("Atualizando sala: {}", id);
        
        return salaRepository.findById(id)
            .map(sala -> {
                // Atualizar psicólogo responsável se fornecido
                if (request.getPsicologoResponsavelId() != null) {
                    Psicologo psicologo = psicologoRepository.findById(request.getPsicologoResponsavelId())
                        .orElseThrow(() -> new IllegalArgumentException("Psicólogo não encontrado"));
                    sala.setPsicologoResponsavel(psicologo);
                }
                
                sala.setNome(request.getNome());
                sala.setNumero(request.getNumero());
                sala.setDescricao(request.getDescricao());
                sala.setCapacidade(request.getCapacidade());
                
                if (request.getAtiva() != null) {
                    sala.setAtiva(request.getAtiva());
                }
                if (request.getCor() != null) {
                    sala.setCor(request.getCor());
                }
                if (request.getExclusiva() != null) {
                    sala.setExclusiva(request.getExclusiva());
                }
                if (request.getPermiteCompartilhamento() != null) {
                    sala.setPermiteCompartilhamento(request.getPermiteCompartilhamento());
                }
                
                sala.setAndar(request.getAndar());
                sala.setBloco(request.getBloco());
                sala.setObservacoes(request.getObservacoes());
                
                return salaRepository.save(sala);
            });
    }

    @Transactional
    public boolean deletar(Long id) {
        log.info("Deletando sala: {}", id);
        return salaRepository.findById(id)
            .map(sala -> {
                // Verificar se há sessões agendadas
                List<Sessao> sessoesAgendadas = sessaoRepository.findBySalaIdAndStatusTrue(id.intValue());
                if (!sessoesAgendadas.isEmpty()) {
                    throw new IllegalStateException(
                        "Não é possível excluir a sala. Existem " + sessoesAgendadas.size() + 
                        " sessão(ões) agendada(s) nesta sala."
                    );
                }
                salaRepository.delete(sala);
                return true;
            })
            .orElse(false);
    }

    /**
     * Verifica se uma sala está disponível em determinado horário
     */
    public boolean verificarDisponibilidade(Long salaId, LocalDate data, LocalTime hora) {
        log.info("Verificando disponibilidade - Sala: {}, Data: {}, Hora: {}", salaId, data, hora);
        
        // Verificar se sala existe e está ativa
        Sala sala = salaRepository.findById(salaId)
            .orElseThrow(() -> new IllegalArgumentException("Sala não encontrada"));
        
        if (!sala.getAtiva()) {
            return false;
        }
        
        // Verificar se há sessões conflitantes (mesma data e hora)
        List<Sessao> sessoesConflitantes = sessaoRepository.findBySalaIdAndDataAndHora(
            salaId.intValue(), data, hora
        );
        
        return sessoesConflitantes.isEmpty();
    }

    /**
     * Lista salas disponíveis em determinado horário
     */
    public List<Sala> listarSalasDisponiveis(Long clinicaId, LocalDate data, LocalTime hora) {
        log.info("Listando salas disponíveis - Clinica: {}, Data: {}, Hora: {}", 
            clinicaId, data, hora);
        
        List<Sala> salasAtivas = listarSalasAtivas(clinicaId);
        
        return salasAtivas.stream()
            .filter(sala -> verificarDisponibilidade(sala.getId(), data, hora))
            .collect(Collectors.toList());
    }

    /**
     * Lista salas disponíveis para um psicólogo específico
     */
    public List<Sala> listarSalasDisponiveisPorPsicologo(
            Long clinicaId, Long psicologoId, LocalDate data, LocalTime hora) {
        log.info("Listando salas disponíveis - Clinica: {}, Psicólogo: {}, Data: {}, Hora: {}", 
            clinicaId, psicologoId, data, hora);
        
        List<Sala> salasAtivas = listarSalasAtivas(clinicaId);
        
        return salasAtivas.stream()
            .filter(sala -> sala.isPsicologoAutorizado(psicologoId))
            .filter(sala -> verificarDisponibilidade(sala.getId(), data, hora))
            .collect(Collectors.toList());
    }

    /**
     * Lista salas de um psicólogo específico (sala preferencial/responsável)
     */
    public List<Sala> listarSalasPorPsicologo(Long psicologoId) {
        log.info("Listando salas do psicólogo: {}", psicologoId);
        return salaRepository.findAll().stream()
            .filter(sala -> sala.getPsicologoResponsavel() != null && 
                           sala.getPsicologoResponsavel().getId().equals(psicologoId))
            .collect(Collectors.toList());
    }

    /**
     * Busca sala preferencial/padrão do psicólogo
     */
    public Optional<Sala> buscarSalaPreferencialPsicologo(Long psicologoId) {
        log.info("Buscando sala preferencial do psicólogo: {}", psicologoId);
        List<Sala> salas = listarSalasPorPsicologo(psicologoId);
        // Retorna a primeira sala exclusiva ou a primeira sala do psicólogo
        return salas.stream()
            .filter(Sala::getExclusiva)
            .findFirst()
            .or(() -> salas.stream().findFirst());
    }

    /**
     * Retorna estatísticas de uso de uma sala
     */
    public SalaEstatisticas getEstatisticas(Long salaId, LocalDate dataInicio, LocalDate dataFim) {
        log.info("Gerando estatísticas - Sala: {}, Período: {} a {}", 
            salaId, dataInicio, dataFim);
        
        Sala sala = salaRepository.findById(salaId)
            .orElseThrow(() -> new IllegalArgumentException("Sala não encontrada"));
        
        List<Sessao> sessoes = sessaoRepository.findBySalaIdAndDataBetween(
            salaId.intValue(), dataInicio, dataFim
        );
        
        return SalaEstatisticas.builder()
            .salaId(salaId)
            .salaNome(sala.getNome())
            .totalSessoes((long) sessoes.size())
            .periodoInicio(dataInicio)
            .periodoFim(dataFim)
            .build();
    }

    // DTO para estatísticas
    @lombok.Data
    @lombok.Builder
    public static class SalaEstatisticas {
        private Long salaId;
        private String salaNome;
        private Long totalSessoes;
        private LocalDate periodoInicio;
        private LocalDate periodoFim;
    }
}

