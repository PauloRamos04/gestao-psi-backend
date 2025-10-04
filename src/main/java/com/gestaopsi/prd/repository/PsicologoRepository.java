package com.gestaopsi.prd.repository;

import com.gestaopsi.prd.entity.Psicologo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PsicologoRepository extends JpaRepository<Psicologo, Long> {
    Optional<Psicologo> findByPsicologLogin(String psicologLogin);
}


