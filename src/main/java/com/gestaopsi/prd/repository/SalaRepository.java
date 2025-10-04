package com.gestaopsi.prd.repository;

import com.gestaopsi.prd.entity.Sala;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SalaRepository extends JpaRepository<Sala, Long> {
    List<Sala> findByClinicaId(Integer clinicaId);
}


