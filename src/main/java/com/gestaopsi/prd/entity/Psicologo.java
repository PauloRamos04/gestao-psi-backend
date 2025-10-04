package com.gestaopsi.prd.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "psicologos")
public class Psicologo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "psicolog_login", nullable = false, unique = true)
    private String psicologLogin;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "dt_ativacao")
    private LocalDate dtAtivacao;

    @Column(name = "categoria_id")
    private Integer categoriaId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPsicologLogin() { return psicologLogin; }
    public void setPsicologLogin(String psicologLogin) { this.psicologLogin = psicologLogin; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public LocalDate getDtAtivacao() { return dtAtivacao; }
    public void setDtAtivacao(LocalDate dtAtivacao) { this.dtAtivacao = dtAtivacao; }
    public Integer getCategoriaId() { return categoriaId; }
    public void setCategoriaId(Integer categoriaId) { this.categoriaId = categoriaId; }
}


