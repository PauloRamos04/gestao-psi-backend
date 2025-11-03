package com.gestaopsi.prd.service;

import com.gestaopsi.prd.entity.Clinica;
import com.gestaopsi.prd.entity.Sublocacao;
import com.gestaopsi.prd.repository.ClinicaRepository;
import com.gestaopsi.prd.repository.SublocacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SublocacaoService {

    private final SublocacaoRepository sublocacaoRepository;
    private final ClinicaRepository clinicaRepository;

    public List<Sublocacao> listarPorClinica(Long clinicaId) {
        return sublocacaoRepository.findByClinicaId(clinicaId);
    }

    @Transactional
    public Sublocacao criar(Long clinicaId, Sublocacao s) {
        Clinica clinica = clinicaRepository.findById(clinicaId)
                .orElseThrow(() -> new IllegalArgumentException("Clínica não encontrada"));
        s.setClinica(clinica);
        return sublocacaoRepository.save(s);
    }

    @Transactional
    public Sublocacao atualizar(Long id, Sublocacao s) {
        Sublocacao atual = sublocacaoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sublocação não encontrada"));
        atual.setRoomName(s.getRoomName());
        atual.setTenantName(s.getTenantName());
        atual.setTenantType(s.getTenantType());
        atual.setStartDate(s.getStartDate());
        atual.setEndDate(s.getEndDate());
        atual.setMonthlyRate(s.getMonthlyRate());
        atual.setStatus(s.getStatus());
        return sublocacaoRepository.save(atual);
    }

    @Transactional
    public void deletar(Long id) {
        sublocacaoRepository.deleteById(id);
    }
}











