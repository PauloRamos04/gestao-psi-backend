package com.gestaopsi.prd.service;

import com.gestaopsi.prd.entity.Sessao;
import com.gestaopsi.prd.repository.SessaoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SessaoService {

    private final SessaoRepository sessaoRepository;

    public SessaoService(SessaoRepository sessaoRepository) {
        this.sessaoRepository = sessaoRepository;
    }

    public List<Sessao> listarPorPeriodo(Integer clinicaId, Integer psicologId, LocalDate inicio, LocalDate fim) {
        return sessaoRepository.findByClinicaIdAndPsicologIdAndDataBetween(clinicaId, psicologId, inicio, fim);
    }

    public List<Sessao> listarPorData(Integer clinicaId, Integer psicologId, LocalDate data) {
        return sessaoRepository.findByClinicaIdAndPsicologIdAndData(clinicaId, psicologId, data);
    }
}


