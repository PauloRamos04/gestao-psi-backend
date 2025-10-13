package com.gestaopsi.prd.repository;

import com.gestaopsi.prd.entity.Sublocacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SublocacaoRepository extends JpaRepository<Sublocacao, Long> {
    List<Sublocacao> findByClinicaId(Long clinicaId);
}





