package com.gestaopsi.prd.repository;

import com.gestaopsi.prd.entity.InteracaoIndicacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InteracaoIndicacaoRepository extends JpaRepository<InteracaoIndicacao, Long> {}


