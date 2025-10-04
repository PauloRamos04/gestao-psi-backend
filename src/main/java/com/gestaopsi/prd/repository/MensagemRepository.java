package com.gestaopsi.prd.repository;

import com.gestaopsi.prd.entity.Mensagem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MensagemRepository extends JpaRepository<Mensagem, Long> {
    List<Mensagem> findByStatusTrueOrderByDataCriacaoDesc();
}
