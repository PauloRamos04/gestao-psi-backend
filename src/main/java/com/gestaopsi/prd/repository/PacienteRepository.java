package com.gestaopsi.prd.repository;

import com.gestaopsi.prd.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    List<Paciente> findByClinicaIdAndPsicologIdAndStatusTrue(Integer clinicaId, Integer psicologId);
}


