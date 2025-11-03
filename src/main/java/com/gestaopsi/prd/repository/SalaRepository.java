package com.gestaopsi.prd.repository;

import com.gestaopsi.prd.entity.Sala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SalaRepository extends JpaRepository<Sala, Long> {
    
    /**
     * Busca salas por clínica com relacionamentos carregados (JOIN FETCH)
     * para evitar LazyInitializationException ao serializar para JSON
     */
    @Query("SELECT DISTINCT s FROM Sala s LEFT JOIN FETCH s.clinica LEFT JOIN FETCH s.psicologoResponsavel WHERE s.clinica.id = :clinicaId")
    List<Sala> findByClinicaId(@Param("clinicaId") Long clinicaId);
    
    /**
     * Busca sala por ID com relacionamentos carregados
     */
    @Query("SELECT DISTINCT s FROM Sala s LEFT JOIN FETCH s.clinica LEFT JOIN FETCH s.psicologoResponsavel WHERE s.id = :id")
    Optional<Sala> findByIdWithRelations(@Param("id") Long id);
    
    /**
     * Busca todas as salas com relacionamentos carregados (JOIN FETCH)
     * para evitar LazyInitializationException ao serializar para JSON
     */
    @Query("SELECT DISTINCT s FROM Sala s LEFT JOIN FETCH s.clinica LEFT JOIN FETCH s.psicologoResponsavel")
    List<Sala> findAllWithRelations();
}


