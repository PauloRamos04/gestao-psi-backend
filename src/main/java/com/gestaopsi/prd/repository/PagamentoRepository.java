package com.gestaopsi.prd.repository;

import com.gestaopsi.prd.entity.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
    
    @Query("SELECT p FROM Pagamento p WHERE " +
           "p.clinica.id = :clinicaId AND " +
           "p.psicologo.id = :psicologId AND " +
           "p.data BETWEEN :inicio AND :fim")
    List<Pagamento> findByClinicaIdAndPsicologIdAndDataBetween(
        @Param("clinicaId") Integer clinicaId,
        @Param("psicologId") Integer psicologId,
        @Param("inicio") LocalDate inicio,
        @Param("fim") LocalDate fim
    );
    
    @Query("SELECT SUM(p.valor) FROM Pagamento p WHERE " +
           "p.clinica.id = :clinicaId AND " +
           "p.psicologo.id = :psicologId AND " +
           "p.data BETWEEN :inicio AND :fim")
    Double calcularFaturamentoPorPeriodo(
        @Param("clinicaId") Integer clinicaId,
        @Param("psicologId") Integer psicologId,
        @Param("inicio") LocalDate inicio,
        @Param("fim") LocalDate fim
    );
}
