package com.gestaopsi.prd.service;

import com.gestaopsi.prd.dto.PagamentoRequest;
import com.gestaopsi.prd.entity.*;
import com.gestaopsi.prd.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PagamentoServiceImpl {

    private final PagamentoRepository pagamentoRepository;
    private final ClinicaRepository clinicaRepository;
    private final PsicologoRepository psicologoRepository;
    private final PacienteRepository pacienteRepository;
    private final TipoPagamentoRepository tipoPagamentoRepository;

    public List<Pagamento> listarPorPeriodo(
            Long clinicaId, 
            Long psicologId, 
            LocalDate inicio, 
            LocalDate fim) {
        
        log.info("Listando pagamentos por período - Clinica: {}, Psicólogo: {}, Período: {} a {}", 
            clinicaId, psicologId, inicio, fim);
        
        return pagamentoRepository.findByClinicaIdAndPsicologIdAndDataBetween(
            clinicaId.intValue(), 
            psicologId.intValue(), 
            inicio, 
            fim
        );
    }

    public Optional<Pagamento> buscarPorId(Long id) {
        return pagamentoRepository.findById(id);
    }

    @Transactional
    public Pagamento criar(PagamentoRequest request) {
        log.info("Criando novo pagamento - Paciente ID: {}, Valor: {}", 
            request.getPacienteId(), request.getValor());
        
        Clinica clinica = clinicaRepository.findById(request.getClinicaId())
            .orElseThrow(() -> new IllegalArgumentException("Clínica não encontrada"));
        
        Psicologo psicologo = psicologoRepository.findById(request.getPsicologId())
            .orElseThrow(() -> new IllegalArgumentException("Psicólogo não encontrado"));
        
        Paciente paciente = pacienteRepository.findById(request.getPacienteId())
            .orElseThrow(() -> new IllegalArgumentException("Paciente não encontrado"));
        
        TipoPagamento tipoPagamento = tipoPagamentoRepository.findById(request.getTipoPagamentoId())
            .orElseThrow(() -> new IllegalArgumentException("Tipo de pagamento não encontrado"));
        
        Pagamento pagamento = Pagamento.builder()
            .clinica(clinica)
            .psicologo(psicologo)
            .paciente(paciente)
            .tipoPagamento(tipoPagamento)
            .valor(request.getValor())
            .data(request.getData())
            .observacoes(request.getObservacoes())
            // Campos de Convênio
            .ehConvenio(request.getEhConvenio() != null ? request.getEhConvenio() : false)
            .convenio(request.getConvenio())
            .numeroGuia(request.getNumeroGuia())
            .valorConvenio(request.getValorConvenio())
            .valorCoparticipacao(request.getValorCoparticipacao())
            .build();
        
        return pagamentoRepository.save(pagamento);
    }

    @Transactional
    public Optional<Pagamento> atualizar(Long id, PagamentoRequest request) {
        log.info("Atualizando pagamento: {}", id);
        
        return pagamentoRepository.findById(id)
            .map(pagamento -> {
                pagamento.setValor(request.getValor());
                pagamento.setData(request.getData());
                pagamento.setObservacoes(request.getObservacoes());
                
                // Atualizar campos de convênio
                pagamento.setEhConvenio(request.getEhConvenio() != null ? request.getEhConvenio() : false);
                pagamento.setConvenio(request.getConvenio());
                pagamento.setNumeroGuia(request.getNumeroGuia());
                pagamento.setValorConvenio(request.getValorConvenio());
                pagamento.setValorCoparticipacao(request.getValorCoparticipacao());
                
                if (request.getTipoPagamentoId() != null) {
                    TipoPagamento tipoPagamento = tipoPagamentoRepository
                        .findById(request.getTipoPagamentoId())
                        .orElseThrow(() -> new IllegalArgumentException("Tipo de pagamento não encontrado"));
                    pagamento.setTipoPagamento(tipoPagamento);
                }
                
                return pagamentoRepository.save(pagamento);
            });
    }

    @Transactional
    public boolean deletar(Long id) {
        log.info("Deletando pagamento: {}", id);
        return pagamentoRepository.findById(id)
            .map(pagamento -> {
                pagamentoRepository.delete(pagamento);
                return true;
            })
            .orElse(false);
    }

    public Double calcularFaturamento(Long clinicaId, Long psicologId, LocalDate inicio, LocalDate fim) {
        Double faturamento = pagamentoRepository.calcularFaturamentoPorPeriodo(
            clinicaId.intValue(), 
            psicologId.intValue(), 
            inicio, 
            fim
        );
        return faturamento != null ? faturamento : 0.0;
    }
}

