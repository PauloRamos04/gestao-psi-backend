package com.gestaopsi.prd.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "users", indexes = {
    @Index(name = "idx_users_username", columnList = "username"),
    @Index(name = "idx_users_email", columnList = "email"),
    @Index(name = "idx_users_status", columnList = "status")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true, nullable = false, length = 50)
    private String username;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clinica_id", nullable = false)
    private Clinica clinica;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "psicolog_id", nullable = false)
    private Psicologo psicologo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_id", nullable = false)
    private TipoUser tipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(name = "senha", nullable = false)
    private String senha;

    @Column(name = "status", nullable = false)
    @Builder.Default
    private Boolean status = true;

    @Column(name = "titulo", length = 100)
    private String titulo;

    // Informações de Contato
    @Column(name = "email", unique = true, length = 100)
    private String email;

    @Column(name = "telefone", length = 20)
    private String telefone;

    @Column(name = "celular", length = 20)
    private String celular;

    // Informações Pessoais
    @Column(name = "nome_completo", length = 200)
    private String nomeCompleto;

    @Column(name = "foto_url", length = 500)
    private String fotoUrl;

    @Column(name = "cargo", length = 100)
    private String cargo; // Ex: Psicólogo, Recepcionista, Gerente

    @Column(name = "departamento", length = 100)
    private String departamento;

    // Controle de Acesso
    @Column(name = "ultimo_acesso")
    private LocalDateTime ultimoAccesso;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @Column(name = "criado_por", length = 50)
    private String criadoPor;

    @Column(name = "data_inativacao")
    private LocalDateTime dataInativacao;

    @Column(name = "motivo_inativacao", length = 500)
    private String motivoInativacao;

    // Preferências do Sistema
    @Column(name = "tema_preferido", length = 20)
    @Builder.Default
    private String temaPreferido = "light"; // light, dark

    @Column(name = "idioma", length = 5)
    @Builder.Default
    private String idioma = "pt-BR";

    @Column(name = "timezone", length = 50)
    @Builder.Default
    private String timezone = "America/Sao_Paulo";

    @Column(name = "receber_notificacoes_email")
    @Builder.Default
    private Boolean receberNotificacoesEmail = true;

    @Column(name = "receber_notificacoes_sistema")
    @Builder.Default
    private Boolean receberNotificacoesSistema = true;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    // Compatibilidade
    @Transient
    public Integer getClinicaId() {
        return clinica != null ? clinica.getId().intValue() : null;
    }

    @Transient
    public Integer getPsicologId() {
        return psicologo != null ? psicologo.getId().intValue() : null;
    }

    @Transient
    public Integer getTipoId() {
        return tipo != null ? tipo.getId().intValue() : null;
    }
}
