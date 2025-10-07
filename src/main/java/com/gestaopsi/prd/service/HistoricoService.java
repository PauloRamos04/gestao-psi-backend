package com.gestaopsi.prd.service;

import com.gestaopsi.prd.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class HistoricoService {

    private final SessaoRepository sessaoRepository;
    private final PagamentoRepository pagamentoRepository;
    private final PacienteRepository pacienteRepository;

    public Map<String, Object> getDashboardData(Long clinicaId, Long psicologId, LocalDate inicio, LocalDate fim) {
        Map<String, Object> dashboard = new HashMap<>();
        
        // Total de sessões
        var sessoes = sessaoRepository.findByClinicaIdAndPsicologIdAndDataBetween(
            clinicaId.intValue(), psicologId.intValue(), inicio, fim);
        dashboard.put("totalSessoes", sessoes.size());
        
        // Faturamento total
        Double faturamento = pagamentoRepository.calcularFaturamentoPorPeriodo(
            clinicaId.intValue(), psicologId.intValue(), inicio, fim);
        dashboard.put("totalFaturamento", faturamento != null ? faturamento : 0.0);
        
        // Total de pacientes ativos
        var pacientes = pacienteRepository.findByClinicaIdAndPsicologIdAndStatusTrue(
            clinicaId.intValue(), psicologId.intValue());
        dashboard.put("totalPacientes", pacientes.size());
        
        // Sessões confirmadas vs canceladas
        long confirmadas = sessoes.stream().filter(s -> "CONFIRMADA".equals(s.getStatus())).count();
        long canceladas = sessoes.stream().filter(s -> "CANCELADA".equals(s.getStatus())).count();
        dashboard.put("sessoesConfirmadas", confirmadas);
        dashboard.put("sessoesCanceladas", canceladas);
        
        return dashboard;
    }

    public List<Map<String, Object>> getTimelineData(Long clinicaId, Long psicologId, Integer months) {
        List<Map<String, Object>> timeline = new ArrayList<>();
        LocalDate fim = LocalDate.now();
        LocalDate inicio = fim.minusMonths(months);
        
        YearMonth current = YearMonth.from(inicio);
        YearMonth end = YearMonth.from(fim);
        
        while (!current.isAfter(end)) {
            Map<String, Object> monthData = new HashMap<>();
            
            LocalDate monthStart = current.atDay(1);
            LocalDate monthEnd = current.atEndOfMonth();
            
            var sessoes = sessaoRepository.findByClinicaIdAndPsicologIdAndDataBetween(
                clinicaId.intValue(), psicologId.intValue(), monthStart, monthEnd);
            
            Double faturamento = pagamentoRepository.calcularFaturamentoPorPeriodo(
                clinicaId.intValue(), psicologId.intValue(), monthStart, monthEnd);
            
            monthData.put("month", current.toString());
            monthData.put("monthName", getMonthName(current.getMonthValue()));
            monthData.put("year", current.getYear());
            monthData.put("sessions", sessoes.size());
            monthData.put("revenue", faturamento != null ? faturamento : 0.0);
            
            timeline.add(monthData);
            current = current.plusMonths(1);
        }
        
        return timeline;
    }

    public List<Map<String, Object>> getSalasUsage(Long clinicaId, Long psicologId, LocalDate inicio, LocalDate fim) {
        var sessoes = sessaoRepository.findByClinicaIdAndPsicologIdAndDataBetween(
            clinicaId.intValue(), psicologId.intValue(), inicio, fim);
        
        Map<String, Long> usage = sessoes.stream()
            .filter(s -> s.getSala() != null)
            .collect(Collectors.groupingBy(
                s -> s.getSala().getNome(),
                Collectors.counting()
            ));
        
        long total = sessoes.size();
        
        return usage.entrySet().stream()
            .map(entry -> {
                Map<String, Object> salaData = new HashMap<>();
                salaData.put("sala", entry.getKey());
                salaData.put("count", entry.getValue());
                salaData.put("percentage", total > 0 ? (entry.getValue() * 100.0 / total) : 0.0);
                return salaData;
            })
            .sorted((a, b) -> Long.compare((Long)b.get("count"), (Long)a.get("count")))
            .collect(Collectors.toList());
    }

    public List<Map<String, Object>> getPacientesEvolucao(Long clinicaId, Long psicologId, Integer months) {
        List<Map<String, Object>> evolucao = new ArrayList<>();
        LocalDate fim = LocalDate.now();
        LocalDate inicio = fim.minusMonths(months);
        
        YearMonth current = YearMonth.from(inicio);
        YearMonth end = YearMonth.from(fim);
        
        while (!current.isAfter(end)) {
            Map<String, Object> monthData = new HashMap<>();
            
            var pacientes = pacienteRepository.findByClinicaIdAndPsicologIdAndStatusTrue(
                clinicaId.intValue(), psicologId.intValue());
            
            monthData.put("month", current.toString());
            monthData.put("monthName", getMonthName(current.getMonthValue()));
            monthData.put("total", pacientes.size());
            monthData.put("novos", 0);
            
            evolucao.add(monthData);
            current = current.plusMonths(1);
        }
        
        return evolucao;
    }

    public List<Map<String, Object>> getFinanceiroEvolucao(Long clinicaId, Long psicologId, Integer months) {
        List<Map<String, Object>> evolucao = new ArrayList<>();
        LocalDate fim = LocalDate.now();
        LocalDate inicio = fim.minusMonths(months);
        
        YearMonth current = YearMonth.from(inicio);
        YearMonth end = YearMonth.from(fim);
        
        while (!current.isAfter(end)) {
            Map<String, Object> monthData = new HashMap<>();
            
            LocalDate monthStart = current.atDay(1);
            LocalDate monthEnd = current.atEndOfMonth();
            
            Double faturamento = pagamentoRepository.calcularFaturamentoPorPeriodo(
                clinicaId.intValue(), psicologId.intValue(), monthStart, monthEnd);
            
            var pagamentos = pagamentoRepository.findByClinicaIdAndPsicologIdAndDataBetween(
                clinicaId.intValue(), psicologId.intValue(), monthStart, monthEnd);
            
            monthData.put("month", current.toString());
            monthData.put("monthName", getMonthName(current.getMonthValue()));
            monthData.put("receita", faturamento != null ? faturamento : 0.0);
            monthData.put("pagamentos", pagamentos.size());
            
            evolucao.add(monthData);
            current = current.plusMonths(1);
        }
        
        return evolucao;
    }

    private String getMonthName(int month) {
        String[] months = {"Jan", "Fev", "Mar", "Abr", "Mai", "Jun", 
                          "Jul", "Ago", "Set", "Out", "Nov", "Dez"};
        return months[month - 1];
    }
}

