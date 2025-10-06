package com.gestaopsi.prd.service;

import com.gestaopsi.prd.dto.PsicologoRequest;
import com.gestaopsi.prd.entity.Categoria;
import com.gestaopsi.prd.entity.Psicologo;
import com.gestaopsi.prd.repository.CategoriaRepository;
import com.gestaopsi.prd.repository.PsicologoRepository;
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
public class PsicologoService {

    private final PsicologoRepository psicologoRepository;
    private final CategoriaRepository categoriaRepository;

    public List<Psicologo> listAll() {
        log.info("Listando todos os psicólogos");
        return psicologoRepository.findAll();
    }

    public Optional<Psicologo> findByLogin(String login) {
        return psicologoRepository.findByPsicologLogin(login);
    }

    @Transactional
    public Psicologo criar(PsicologoRequest request) {
        log.info("Criando novo psicólogo: {}", request.getNome());
        
        Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada"));
        
        Psicologo psicologo = Psicologo.builder()
                .psicologLogin(request.getPsicologLogin())
                .nome(request.getNome())
                .dtAtivacao(request.getDtAtivacao() != null ? request.getDtAtivacao() : LocalDate.now())
                .categoria(categoria)
                .build();
        
        return psicologoRepository.save(psicologo);
    }

    @Transactional
    public Optional<Psicologo> atualizar(Long id, PsicologoRequest request) {
        log.info("Atualizando psicólogo: {}", id);
        
        return psicologoRepository.findById(id)
                .map(psicologo -> {
                    psicologo.setPsicologLogin(request.getPsicologLogin());
                    psicologo.setNome(request.getNome());
                    
                    if (request.getDtAtivacao() != null) {
                        psicologo.setDtAtivacao(request.getDtAtivacao());
                    }
                    
                    if (request.getCategoriaId() != null) {
                        Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                                .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada"));
                        psicologo.setCategoria(categoria);
                    }
                    
                    return psicologoRepository.save(psicologo);
                });
    }

    @Transactional
    public boolean deletar(Long id) {
        log.info("Deletando psicólogo: {}", id);
        return psicologoRepository.findById(id)
                .map(psicologo -> {
                    psicologoRepository.delete(psicologo);
                    return true;
                })
                .orElse(false);
    }
}


