package com.gestaopsi.prd.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "notificacoes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(length = 200, nullable = false)
    private String titulo;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String mensagem;

    @Column(length = 50)
    private String tipo; // INFO, ALERTA, URGENTE, SESSAO, PAGAMENTO

    @Column(nullable = false)
    private LocalDateTime dataCriacao;

    @Column
    private LocalDateTime dataLeitura;

    @Column(nullable = false)
    private Boolean lida = false;

    @Column
    private String link; // Link relacionado

    @Column
    private Boolean enviada = false;

    @Column
    private LocalDateTime dataEnvio;
}

