package com.gestaopsi.prd.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "salas", indexes = {
    @Index(name = "idx_salas_clinica", columnList = "clinica_id"),
    @Index(name = "idx_salas_status", columnList = "ativa")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sala {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clinica_id", nullable = false)
    private Clinica clinica;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "psicologo_responsavel_id")
    private Psicologo psicologoResponsavel; // Psicólogo "dono" ou responsável pela sala (opcional)

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "numero", length = 20)
    private String numero; // Ex: "101", "Sala A", "Consultório 1"

    @Column(name = "descricao", length = 500)
    private String descricao; // Descrição/equipamentos disponíveis

    @Column(name = "capacidade")
    private Integer capacidade; // Número de pessoas

    @Column(name = "ativa", nullable = false)
    @Builder.Default
    private Boolean ativa = true; // Ativa/Inativa/Em manutenção

    @Column(name = "cor", length = 7)
    @Builder.Default
    private String cor = "#3B82F6"; // Cor para calendário (hex)

    @Column(name = "andar")
    private Integer andar; // Andar do prédio

    @Column(name = "bloco", length = 50)
    private String bloco; // Bloco/Ala (se aplicável)

    @Column(name = "observacoes", length = 500)
    private String observacoes;

    @Column(name = "exclusiva")
    @Builder.Default
    private Boolean exclusiva = false; // Se true, apenas o psicólogo responsável pode usar

    @Column(name = "permite_compartilhamento")
    @Builder.Default
    private Boolean permiteCompartilhamento = true; // Permite que outros psicólogos usem

    // Compatibilidade com código existente
    @Transient
    public Integer getClinicaId() {
        return clinica != null ? clinica.getId().intValue() : null;
    }

    @Transient
    public Integer getPsicologoResponsavelId() {
        return psicologoResponsavel != null ? psicologoResponsavel.getId().intValue() : null;
    }

    @Transient
    public boolean isPsicologoAutorizado(Long psicologoId) {
        // Se não é exclusiva, qualquer um pode usar
        if (!exclusiva || !ativa) {
            return ativa;
        }
        
        // Se é exclusiva, só o responsável pode usar
        if (psicologoResponsavel != null) {
            return psicologoResponsavel.getId().equals(psicologoId);
        }
        
        // Se é exclusiva mas não tem responsável definido, ninguém usa
        return false;
    }

    @Transient
    public String getDescricaoCompleta() {
        StringBuilder desc = new StringBuilder(nome);
        if (numero != null && !numero.isBlank()) {
            desc.append(" (").append(numero).append(")");
        }
        if (andar != null) {
            desc.append(" - ").append(andar).append("º andar");
        }
        if (bloco != null && !bloco.isBlank()) {
            desc.append(" - Bloco ").append(bloco);
        }
        return desc.toString();
    }
}
