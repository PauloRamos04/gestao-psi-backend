package com.gestaopsi.prd.service;

import com.gestaopsi.prd.entity.InteracaoIndicacao;
import com.gestaopsi.prd.entity.InteracaoSugestao;
import com.gestaopsi.prd.repository.InteracaoIndicacaoRepository;
import com.gestaopsi.prd.repository.InteracaoSugestaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InteracoesService {
    private final InteracaoSugestaoRepository sugestaoRepository;
    private final InteracaoIndicacaoRepository indicacaoRepository;

    public List<InteracaoSugestao> listarSugestoes() { return sugestaoRepository.findAll(); }
    public List<InteracaoIndicacao> listarIndicacoes() { return indicacaoRepository.findAll(); }

    @Transactional
    public InteracaoSugestao criarSugestao(InteracaoSugestao s) { return sugestaoRepository.save(s); }

    @Transactional
    public InteracaoIndicacao criarIndicacao(InteracaoIndicacao i) { return indicacaoRepository.save(i); }
}











