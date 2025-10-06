package com.gestaopsi.prd.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "clinicas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Clinica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "clinica_login", unique = true, nullable = false, length = 50)
    private String clinicaLogin;

    @Column(name = "nome", nullable = false, length = 200)
    private String nome;

    @Column(name = "status", nullable = false)
    private Boolean status = true;

    @Column(name = "titulo", length = 100)
    private String titulo;
}
