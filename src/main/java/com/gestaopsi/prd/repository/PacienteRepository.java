package com.gestaopsi.prd.repository;

import com.gestaopsi.prd.entity.Paciente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    
    @Query("SELECT DISTINCT p FROM Paciente p LEFT JOIN FETCH p.clinica LEFT JOIN FETCH p.psicologo WHERE p.clinica.id = :clinicaId AND p.psicologo.id = :psicologId AND p.status = true")
    List<Paciente> findByClinicaIdAndPsicologIdAndStatusTrue(
        @Param("clinicaId") Integer clinicaId, 
        @Param("psicologId") Integer psicologId
    );
    
    @Query(value = "SELECT DISTINCT p FROM Paciente p LEFT JOIN FETCH p.clinica LEFT JOIN FETCH p.psicologo WHERE p.clinica.id = :clinicaId AND p.psicologo.id = :psicologId AND p.status = true",
           countQuery = "SELECT COUNT(DISTINCT p) FROM Paciente p WHERE p.clinica.id = :clinicaId AND p.psicologo.id = :psicologId AND p.status = true")
    Page<Paciente> findByClinicaIdAndPsicologIdAndStatusTrue(
        @Param("clinicaId") Integer clinicaId, 
        @Param("psicologId") Integer psicologId, 
        Pageable pageable
    );
    
    @Query("SELECT p FROM Paciente p WHERE p.clinica.id = :clinicaId AND p.psicologo.id = :psicologId")
    List<Paciente> findByClinicaIdAndPsicologId(
        @Param("clinicaId") Integer clinicaId, 
        @Param("psicologId") Integer psicologId
    );
    
    // Métodos para importação
    Optional<Paciente> findByCpf(String cpf);
    boolean existsByCpf(String cpf);
    boolean existsByEmail(String email);
}


