package com.gestaopsi.prd.repository;

import com.gestaopsi.prd.entity.Sessao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface SessaoRepository extends JpaRepository<Sessao, Long> {
    
    @Query("SELECT s FROM Sessao s WHERE s.clinica.id = :clinicaId AND s.psicologo.id = :psicologId AND s.data BETWEEN :inicio AND :fim")
    List<Sessao> findByClinicaIdAndPsicologIdAndDataBetween(
        @Param("clinicaId") Integer clinicaId, 
        @Param("psicologId") Integer psicologId, 
        @Param("inicio") LocalDate inicio, 
        @Param("fim") LocalDate fim
    );
    
    @Query("SELECT s FROM Sessao s WHERE s.clinica.id = :clinicaId AND s.psicologo.id = :psicologId AND s.data = :data")
    List<Sessao> findByClinicaIdAndPsicologIdAndData(
        @Param("clinicaId") Integer clinicaId, 
        @Param("psicologId") Integer psicologId, 
        @Param("data") LocalDate data
    );
    
    @Query("SELECT s FROM Sessao s WHERE s.psicologo.id = :psicologId AND s.data = :data AND s.hora = :hora")
    List<Sessao> findByPsicologIdAndDataAndHora(
        @Param("psicologId") Long psicologId,
        @Param("data") LocalDate data,
        @Param("hora") LocalTime hora
    );
    
    @Query("SELECT s FROM Sessao s WHERE s.sala.id = :salaId AND s.data = :data AND s.hora = :hora")
    List<Sessao> findBySalaIdAndDataAndHora(
        @Param("salaId") Long salaId,
        @Param("data") LocalDate data,
        @Param("hora") LocalTime hora
    );
    
    @Query("SELECT s FROM Sessao s WHERE s.paciente.id = :pacienteId AND s.data = :data AND s.hora = :hora")
    List<Sessao> findByPacienteIdAndDataAndHora(
        @Param("pacienteId") Long pacienteId,
        @Param("data") LocalDate data,
        @Param("hora") LocalTime hora
    );
}
