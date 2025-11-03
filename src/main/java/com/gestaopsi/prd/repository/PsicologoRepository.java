package com.gestaopsi.prd.repository;

import com.gestaopsi.prd.entity.Psicologo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PsicologoRepository extends JpaRepository<Psicologo, Long> {
    Optional<Psicologo> findByPsicologLogin(String psicologLogin);
    
    /**
     * Busca todos os psicólogos com categoria carregada (JOIN FETCH)
     * para evitar LazyInitializationException ao serializar para JSON
     */
    @Query("SELECT DISTINCT p FROM Psicologo p LEFT JOIN FETCH p.categoria")
    List<Psicologo> findAllWithCategoria();
}


