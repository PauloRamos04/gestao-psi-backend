package com.gestaopsi.prd.repository;

import com.gestaopsi.prd.entity.Clinica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClinicaRepository extends JpaRepository<Clinica, Long> {
    Optional<Clinica> findByClinicaLoginAndStatusTrue(String clinicaLogin);
}


