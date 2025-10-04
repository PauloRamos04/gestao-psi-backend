package com.gestaopsi.prd.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "sessoes")
public class Sessao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "clinica_id", nullable = false)
    private Integer clinicaId;

    @Column(name = "psicolog_id", nullable = false)
    private Integer psicologId;

    @Column(name = "paciente_id", nullable = false)
    private Integer pacienteId;

    @Column(name = "sala_id")
    private Integer salaId;

    @Column(name = "data", nullable = false)
    private LocalDate data;

    @Column(name = "hora", nullable = false)
    private LocalTime hora;

    @Column(name = "status", nullable = false)
    private Boolean status = true;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getClinicaId() { return clinicaId; }
    public void setClinicaId(Integer clinicaId) { this.clinicaId = clinicaId; }
    public Integer getPsicologId() { return psicologId; }
    public void setPsicologId(Integer psicologId) { this.psicologId = psicologId; }
    public Integer getPacienteId() { return pacienteId; }
    public void setPacienteId(Integer pacienteId) { this.pacienteId = pacienteId; }
    public Integer getSalaId() { return salaId; }
    public void setSalaId(Integer salaId) { this.salaId = salaId; }
    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }
    public LocalTime getHora() { return hora; }
    public void setHora(LocalTime hora) { this.hora = hora; }
    public Boolean getStatus() { return status; }
    public void setStatus(Boolean status) { this.status = status; }
}


