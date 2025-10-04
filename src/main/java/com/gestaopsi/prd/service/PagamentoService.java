package com.gestaopsi.prd.service;

import com.gestaopsi.prd.entity.Pagamento;
import com.gestaopsi.prd.repository.PagamentoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;

    public PagamentoService(PagamentoRepository pagamentoRepository) {
        this.pagamentoRepository = pagamentoRepository;
    }

    public List<Pagamento> listarPorPeriodo(Integer clinicaId, Integer psicologId, LocalDate inicio, LocalDate fim) {
        return pagamentoRepository.findByClinicaIdAndPsicologIdAndDataBetween(clinicaId, psicologId, inicio, fim);
    }
}


