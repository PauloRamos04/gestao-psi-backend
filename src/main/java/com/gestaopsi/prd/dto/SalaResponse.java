package com.gestaopsi.prd.dto;

import com.gestaopsi.prd.entity.Sala;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalaResponse {
    private Long id;
    private Long clinicaId;
    private Long psicologoResponsavelId;
    private String nome;
    private String numero;
    private String descricao;
    private Integer capacidade;
    private Boolean ativa;
    private String cor;
    private Integer andar;
    private String bloco;
    private String observacoes;
    private Boolean exclusiva;
    private Boolean permiteCompartilhamento;
    
    // Informações extras
    private String clinicaNome;
    private String psicologoResponsavelNome;
    private String descricaoCompleta;

    public static SalaResponse fromEntity(Sala sala) {
        return SalaResponse.builder()
                .id(sala.getId())
                .clinicaId(sala.getClinica() != null ? sala.getClinica().getId() : null)
                .psicologoResponsavelId(sala.getPsicologoResponsavel() != null ? 
                    sala.getPsicologoResponsavel().getId() : null)
                .nome(sala.getNome())
                .numero(sala.getNumero())
                .descricao(sala.getDescricao())
                .capacidade(sala.getCapacidade())
                .ativa(sala.getAtiva())
                .cor(sala.getCor())
                .andar(sala.getAndar())
                .bloco(sala.getBloco())
                .observacoes(sala.getObservacoes())
                .exclusiva(sala.getExclusiva())
                .permiteCompartilhamento(sala.getPermiteCompartilhamento())
                .clinicaNome(sala.getClinica() != null ? sala.getClinica().getNome() : null)
                .psicologoResponsavelNome(sala.getPsicologoResponsavel() != null ? 
                    sala.getPsicologoResponsavel().getNome() : null)
                .descricaoCompleta(sala.getDescricaoCompleta())
                .build();
    }
}

