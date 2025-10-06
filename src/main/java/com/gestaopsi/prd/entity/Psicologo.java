package com.gestaopsi.prd.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "psicologos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Psicologo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "psicolog_login", unique = true, nullable = false, length = 50)
    private String psicologLogin;

    @Column(name = "nome", nullable = false, length = 200)
    private String nome;

    @Column(name = "dt_ativacao")
    private LocalDate dtAtivacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    // Compatibilidade
    @Transient
    public Integer getCategoriaId() {
        return categoria != null ? categoria.getId().intValue() : null;
    }
}
