package com.gestaopsi.prd.repository;

import com.gestaopsi.prd.entity.TipoPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoPagamentoRepository extends JpaRepository<TipoPagamento, Long> {
}

