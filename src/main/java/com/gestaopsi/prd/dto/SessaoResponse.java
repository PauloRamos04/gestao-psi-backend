package com.gestaopsi.prd.dto;

import com.gestaopsi.prd.entity.Sessao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessaoResponse {
    private Long id;
    private Integer clinicaId;
    private Integer psicologId;
    private Long pacienteId;
    private Long salaId;
    private LocalDate data;
    private LocalTime hora;
    private Boolean status;
    private String observacoes;
    
    // Dados relacionados
    private String clinicaNome;
    private String psicologoNome;
    private String pacienteNome;
    private String salaNome;

    public static SessaoResponse fromEntity(Sessao sessao) {
        return SessaoResponse.builder()
                .id(sessao.getId())
                .clinicaId(sessao.getClinica() != null ? sessao.getClinica().getId().intValue() : null)
                .psicologId(sessao.getPsicologo() != null ? sessao.getPsicologo().getId().intValue() : null)
                .pacienteId(sessao.getPaciente() != null ? sessao.getPaciente().getId() : null)
                .salaId(sessao.getSala() != null ? sessao.getSala().getId() : null)
                .data(sessao.getData())
                .hora(sessao.getHora())
                .status(sessao.getStatus() != null ? sessao.getStatus() : false)
                .observacoes(sessao.getObservacoes())
                .clinicaNome(sessao.getClinica() != null ? sessao.getClinica().getNome() : null)
                .psicologoNome(sessao.getPsicologo() != null ? sessao.getPsicologo().getNome() : null)
                .pacienteNome(sessao.getPaciente() != null ? sessao.getPaciente().getNome() : null)
                .salaNome(sessao.getSala() != null ? sessao.getSala().getNome() : null)
                .build();
    }
}

