package com.gestaopsi.prd.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "pacientes")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "clinica_id", nullable = false)
    private Integer clinicaId;

    @Column(name = "psicolog_id", nullable = false)
    private Integer psicologId;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "status", nullable = false)
    private Boolean status = true;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getClinicaId() { return clinicaId; }
    public void setClinicaId(Integer clinicaId) { this.clinicaId = clinicaId; }
    public Integer getPsicologId() { return psicologId; }
    public void setPsicologId(Integer psicologId) { this.psicologId = psicologId; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public Boolean getStatus() { return status; }
    public void setStatus(Boolean status) { this.status = status; }
}


