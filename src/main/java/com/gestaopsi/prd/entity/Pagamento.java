package com.gestaopsi.prd.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "pagamentos")
public class Pagamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "clinica_id", nullable = false)
    private Integer clinicaId;

    @Column(name = "psicolog_id", nullable = false)
    private Integer psicologId;

    @Column(name = "paciente_id", nullable = false)
    private Integer pacienteId;

    @Column(name = "valor", nullable = false)
    private BigDecimal valor;

    @Column(name = "data", nullable = false)
    private LocalDate data;

    @Column(name = "tipo_pagamento_id")
    private Integer tipoPagamentoId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getClinicaId() { return clinicaId; }
    public void setClinicaId(Integer clinicaId) { this.clinicaId = clinicaId; }
    public Integer getPsicologId() { return psicologId; }
    public void setPsicologId(Integer psicologId) { this.psicologId = psicologId; }
    public Integer getPacienteId() { return pacienteId; }
    public void setPacienteId(Integer pacienteId) { this.pacienteId = pacienteId; }
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }
    public Integer getTipoPagamentoId() { return tipoPagamentoId; }
    public void setTipoPagamentoId(Integer tipoPagamentoId) { this.tipoPagamentoId = tipoPagamentoId; }
}


