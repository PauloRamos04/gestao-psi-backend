package com.gestaopsi.prd.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "prontuarios")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Prontuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    @JsonIgnoreProperties({"prontuarios", "hibernateLazyInitializer", "handler"})
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sessao_id")
    @JsonIgnoreProperties({"prontuarios", "hibernateLazyInitializer", "handler"})
    private Sessao sessao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "psicologo_id", nullable = false)
    @JsonIgnoreProperties({"prontuarios", "hibernateLazyInitializer", "handler"})
    private Psicologo psicologo;

    @Column(nullable = false)
    private LocalDateTime dataRegistro;

    @Column(length = 100)
    private String tipo; // ANAMNESE, EVOLUCAO, OBSERVACAO

    @Column(length = 200)
    private String titulo;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String conteudo;

    @Column(length = 500)
    private String queixaPrincipal;

    @Column(length = 500)
    private String objetivoTerapeutico;

    @Column(columnDefinition = "TEXT")
    private String historico;

    @Column(columnDefinition = "TEXT")
    private String evolucao;

    @Column(columnDefinition = "TEXT")
    private String planoTerapeutico;

    @Column
    private Boolean privado = true;

    @Column
    private Boolean status = true;
}

