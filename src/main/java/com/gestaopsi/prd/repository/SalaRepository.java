package com.gestaopsi.prd.repository;

import com.gestaopsi.prd.entity.Sala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalaRepository extends JpaRepository<Sala, Long> {
    
    @Query("SELECT s FROM Sala s WHERE s.clinica.id = :clinicaId")
    List<Sala> findByClinicaId(@Param("clinicaId") Integer clinicaId);
}


