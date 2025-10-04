package com.gestaopsi.prd.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "clinica_id", nullable = false)
    private Integer clinicaId;

    @Column(name = "psicolog_id", nullable = false)
    private Integer psicologId;

    @Column(name = "tipo_id", nullable = false)
    private Integer tipoId;

    @Column(name = "senha", nullable = false)
    private String senha;

    @Column(name = "status", nullable = false)
    private Boolean status;

    @Column(name = "titulo")
    private String titulo;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getClinicaId() { return clinicaId; }
    public void setClinicaId(Integer clinicaId) { this.clinicaId = clinicaId; }
    public Integer getPsicologId() { return psicologId; }
    public void setPsicologId(Integer psicologId) { this.psicologId = psicologId; }
    public Integer getTipoId() { return tipoId; }
    public void setTipoId(Integer tipoId) { this.tipoId = tipoId; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public Boolean getStatus() { return status; }
    public void setStatus(Boolean status) { this.status = status; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
}


