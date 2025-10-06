package com.gestaopsi.prd.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "sessoes", indexes = {
    @Index(name = "idx_sessoes_data", columnList = "data"),
    @Index(name = "idx_sessoes_psicologo", columnList = "psicolog_id"),
    @Index(name = "idx_sessoes_paciente", columnList = "paciente_id")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sessao {

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
    @JoinColumn(name = "sala_id")
    private Sala sala;

    @Column(name = "data", nullable = false)
    private LocalDate data;

    @Column(name = "hora", nullable = false)
    private LocalTime hora;

    @Column(name = "status", nullable = false)
    private Boolean status = true;

    @Column(name = "observacoes", length = 500)
    private String observacoes;

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
    public Integer getSalaId() {
        return sala != null ? sala.getId().intValue() : null;
    }
}
