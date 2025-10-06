package com.gestaopsi.prd.service;

import com.gestaopsi.prd.dto.MensagemRequest;
import com.gestaopsi.prd.entity.Mensagem;
import com.gestaopsi.prd.repository.MensagemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MensagemServiceImpl {

    private final MensagemRepository mensagemRepository;

    public List<Mensagem> listarAtivas() {
        log.info("Listando mensagens ativas");
        return mensagemRepository.findByStatusTrueOrderByDataCriacaoDesc();
    }

    public List<Mensagem> listarTodas() {
        return mensagemRepository.findAllByOrderByDataCriacaoDesc();
    }

    public Optional<Mensagem> buscarPorId(Long id) {
        return mensagemRepository.findById(id);
    }

    @Transactional
    public Mensagem criar(MensagemRequest request) {
        log.info("Criando nova mensagem: {}", request.getTitulo());
        
        Mensagem mensagem = Mensagem.builder()
            .titulo(request.getTitulo())
            .conteudo(request.getConteudo())
            .dataCriacao(LocalDateTime.now())
            .status(request.getStatus() != null ? request.getStatus() : true)
            .build();
        
        return mensagemRepository.save(mensagem);
    }

    @Transactional
    public Optional<Mensagem> atualizar(Long id, MensagemRequest request) {
        log.info("Atualizando mensagem: {}", id);
        
        return mensagemRepository.findById(id)
            .map(mensagem -> {
                mensagem.setTitulo(request.getTitulo());
                mensagem.setConteudo(request.getConteudo());
                if (request.getStatus() != null) {
                    mensagem.setStatus(request.getStatus());
                }
                return mensagemRepository.save(mensagem);
            });
    }

    @Transactional
    public boolean deletar(Long id) {
        log.info("Deletando mensagem: {}", id);
        return mensagemRepository.findById(id)
            .map(mensagem -> {
                mensagemRepository.delete(mensagem);
                return true;
            })
            .orElse(false);
    }

    @Transactional
    public boolean desativar(Long id) {
        return mensagemRepository.findById(id)
            .map(mensagem -> {
                mensagem.setStatus(false);
                mensagemRepository.save(mensagem);
                return true;
            })
            .orElse(false);
    }
}

