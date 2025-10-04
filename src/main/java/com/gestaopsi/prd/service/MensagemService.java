package com.gestaopsi.prd.service;

import com.gestaopsi.prd.entity.Mensagem;
import com.gestaopsi.prd.repository.MensagemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MensagemService {

    private final MensagemRepository mensagemRepository;

    public MensagemService(MensagemRepository mensagemRepository) {
        this.mensagemRepository = mensagemRepository;
    }

    public List<Mensagem> listarAtivas() {
        return mensagemRepository.findByStatusTrueOrderByDataCriacaoDesc();
    }
}
