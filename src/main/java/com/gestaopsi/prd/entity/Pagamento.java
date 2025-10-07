package com.gestaopsi.prd.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "pagamentos", indexes = {
    @Index(name = "idx_pagamentos_data", columnList = "data"),
    @Index(name = "idx_pagamentos_paciente", columnList = "paciente_id")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clinica_id", nullable = false)
    private Clinica clinica;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "psicolog_id", nullable = false)
    private Psicologo psicologo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_pagamento_id", nullable = false)
    private TipoPagamento tipoPagamento;

    @Column(name = "valor", nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Column(name = "data", nullable = false)
    private LocalDate data;

    @Column(name = "observacoes", length = 500)
    private String observacoes;

    // Campos de Convênio
    @Column(name = "eh_convenio")
    @Builder.Default
    private Boolean ehConvenio = false;

    @Column(name = "convenio", length = 100)
    private String convenio;

    @Column(name = "numero_guia", length = 50)
    private String numeroGuia;

    @Column(name = "valor_convenio", precision = 10, scale = 2)
    private BigDecimal valorConvenio;

    @Column(name = "valor_coparticipacao", precision = 10, scale = 2)
    private BigDecimal valorCoparticipacao;

    // Compatibilidade com código existente
    @Transient
    public Integer getClinicaId() {
        return clinica != null ? clinica.getId().intValue() : null;
    }

    @Transient
    public Integer getPsicologId() {
        return psicologo != null ? psicologo.getId().intValue() : null;
    }

    @Transient
    public Integer getPacienteId() {
        return paciente != null ? paciente.getId().intValue() : null;
    }

    @Transient
    public Integer getTipoPagamentoId() {
        return tipoPagamento != null ? tipoPagamento.getId().intValue() : null;
    }
}
