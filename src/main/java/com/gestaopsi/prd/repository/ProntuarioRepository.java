package com.gestaopsi.prd.repository;

import com.gestaopsi.prd.entity.Prontuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProntuarioRepository extends JpaRepository<Prontuario, Long> {
    
    @Query("SELECT p FROM Prontuario p WHERE p.paciente.id = :pacienteId AND p.status = true ORDER BY p.dataRegistro DESC")
    List<Prontuario> findByPacienteIdAndStatusTrueOrderByDataRegistroDesc(@Param("pacienteId") Long pacienteId);
    
    @Query("SELECT p FROM Prontuario p WHERE p.paciente.id = :pacienteId AND p.status = true ORDER BY p.dataRegistro DESC")
    Page<Prontuario> findByPacienteIdAndStatusTrueOrderByDataRegistroDesc(
        @Param("pacienteId") Long pacienteId, 
        Pageable pageable
    );
    
    @Query("SELECT p FROM Prontuario p WHERE p.psicologo.id = :psicologId AND p.status = true ORDER BY p.dataRegistro DESC")
    List<Prontuario> findByPsicologoIdAndStatusTrueOrderByDataRegistroDesc(@Param("psicologId") Long psicologId);
    
    @Query("SELECT p FROM Prontuario p WHERE p.sessao.id = :sessaoId AND p.status = true")
    List<Prontuario> findBySessaoIdAndStatusTrue(@Param("sessaoId") Long sessaoId);
}

