package com.gestaopsi.prd.repository;

import com.gestaopsi.prd.entity.Sessao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface SessaoRepository extends JpaRepository<Sessao, Long> {
    List<Sessao> findByClinicaIdAndPsicologIdAndDataBetween(Integer clinicaId, Integer psicologId, LocalDate inicio, LocalDate fim);
    List<Sessao> findByClinicaIdAndPsicologIdAndData(Integer clinicaId, Integer psicologId, LocalDate data);
}


