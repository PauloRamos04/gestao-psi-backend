package com.gestaopsi.prd.service;

import com.gestaopsi.prd.dto.RelatorioFinanceiroDTO;
import com.gestaopsi.prd.dto.RelatorioPacientesDTO;
import com.gestaopsi.prd.dto.RelatorioSessoesDTO;
import com.gestaopsi.prd.entity.Pagamento;
import com.gestaopsi.prd.entity.Sessao;
import com.gestaopsi.prd.repository.PacienteRepository;
import com.gestaopsi.prd.repository.PagamentoRepository;
import com.gestaopsi.prd.repository.SessaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RelatorioService {

    private final SessaoRepository sessaoRepository;
    private final PacienteRepository pacienteRepository;
    private final PagamentoRepository pagamentoRepository;

    public RelatorioSessoesDTO gerarRelatorioSessoes(
            Long clinicaId, 
            Long psicologId, 
            LocalDate inicio, 
            LocalDate fim) {
        
        log.info("Gerando relatório de sessões - Clínica: {}, Psicólogo: {}, Período: {} a {}", 
            clinicaId, psicologId, inicio, fim);

        List<Sessao> sessoes = sessaoRepository.findByClinicaIdAndPsicologIdAndDataBetween(
            clinicaId.intValue(),
            psicologId.intValue(),
            inicio,
            fim
        );

        long total = sessoes.size();
        long confirmadas = sessoes.stream().filter(s -> s.getStatus()).count();
        long pendentes = total - confirmadas;
        double taxaConfirmacao = total > 0 ? (confirmadas * 100.0 / total) : 0;

        return RelatorioSessoesDTO.builder()
                .totalSessoes(total)
                .sessoesConfirmadas(confirmadas)
                .sessoesPendentes(pendentes)
                .taxaConfirmacao(taxaConfirmacao)
                .build();
    }

    public RelatorioPacientesDTO gerarRelatorioPacientes(Long clinicaId, Long psicologId) {
        log.info("Gerando relatório de pacientes - Clínica: {}, Psicólogo: {}", clinicaId, psicologId);

        List<com.gestaopsi.prd.entity.Paciente> todosPacientes = 
            pacienteRepository.findByClinicaIdAndPsicologId(
                clinicaId.intValue(),
                psicologId.intValue()
            );

        long total = todosPacientes.size();
        long ativos = todosPacientes.stream().filter(p -> p.getStatus()).count();
        long inativos = total - ativos;
        double percentualAtivos = total > 0 ? (ativos * 100.0 / total) : 0;

        return RelatorioPacientesDTO.builder()
                .totalPacientes(total)
                .pacientesAtivos(ativos)
                .pacientesInativos(inativos)
                .percentualAtivos(percentualAtivos)
                .build();
    }

    public RelatorioFinanceiroDTO gerarRelatorioFinanceiro(
            Long clinicaId, 
            Long psicologId, 
            LocalDate inicio, 
            LocalDate fim) {
        
        log.info("Gerando relatório financeiro - Clínica: {}, Psicólogo: {}, Período: {} a {}", 
            clinicaId, psicologId, inicio, fim);

        List<Pagamento> pagamentos = pagamentoRepository.findByClinicaIdAndPsicologIdAndDataBetween(
            clinicaId.intValue(),
            psicologId.intValue(),
            inicio,
            fim
        );

        double totalRecebido = pagamentos.stream()
                .map(Pagamento::getValor)
                .map(BigDecimal::doubleValue)
                .mapToDouble(Double::doubleValue)
                .sum();

        long quantidade = pagamentos.size();
        double ticketMedio = quantidade > 0 ? totalRecebido / quantidade : 0;

        // Por tipo de pagamento
        Map<String, Double> porTipo = pagamentos.stream()
                .collect(Collectors.groupingBy(
                    p -> p.getTipoPagamento() != null ? p.getTipoPagamento().getNome() : "Não informado",
                    Collectors.summingDouble(p -> p.getValor().doubleValue())
                ));

        // Por mês
        Map<String, Double> porMes = pagamentos.stream()
                .collect(Collectors.groupingBy(
                    p -> p.getData().format(DateTimeFormatter.ofPattern("yyyy-MM")),
                    Collectors.summingDouble(p -> p.getValor().doubleValue())
                ));

        return RelatorioFinanceiroDTO.builder()
                .totalRecebido(totalRecebido)
                .quantidadePagamentos(quantidade)
                .ticketMedio(ticketMedio)
                .porTipoPagamento(porTipo)
                .porMes(porMes)
                .build();
    }
}

