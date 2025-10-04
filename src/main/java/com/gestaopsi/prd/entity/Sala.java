package com.gestaopsi.prd.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "salas")
public class Sala {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "clinica_id", nullable = false)
    private Integer clinicaId;

    @Column(name = "nome", nullable = false)
    private String nome;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getClinicaId() { return clinicaId; }
    public void setClinicaId(Integer clinicaId) { this.clinicaId = clinicaId; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
}


