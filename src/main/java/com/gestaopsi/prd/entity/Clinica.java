package com.gestaopsi.prd.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "clinicas")
public class Clinica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "clinica_login", nullable = false, unique = true)
    private String clinicaLogin;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "status", nullable = false)
    private Boolean status = true;

    @Column(name = "titulo")
    private String titulo;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getClinicaLogin() { return clinicaLogin; }
    public void setClinicaLogin(String clinicaLogin) { this.clinicaLogin = clinicaLogin; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public Boolean getStatus() { return status; }
    public void setStatus(Boolean status) { this.status = status; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
}


