package com.gestaopsi.prd.repository;

import com.gestaopsi.prd.entity.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
    List<Pagamento> findByClinicaIdAndPsicologIdAndDataBetween(Integer clinicaId, Integer psicologId, LocalDate inicio, LocalDate fim);
}


