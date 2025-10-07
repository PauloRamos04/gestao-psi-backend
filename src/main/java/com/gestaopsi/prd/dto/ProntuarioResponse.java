package com.gestaopsi.prd.dto;

import com.gestaopsi.prd.entity.Prontuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProntuarioResponse {
    
    private Long id;
    private Long pacienteId;
    private Long sessaoId;
    private Long psicologoId;
    private LocalDateTime dataRegistro;
    private String tipo;
    private String titulo;
    private String conteudo;
    private String queixaPrincipal;
    private String objetivoTerapeutico;
    private String historico;
    private String evolucao;
    private String planoTerapeutico;
    private Boolean privado;
    private Boolean status;
    
    public static ProntuarioResponse fromEntity(Prontuario prontuario) {
        return ProntuarioResponse.builder()
            .id(prontuario.getId())
            .pacienteId(prontuario.getPaciente().getId())
            .sessaoId(prontuario.getSessao() != null ? prontuario.getSessao().getId() : null)
            .psicologoId(prontuario.getPsicologo().getId())
            .dataRegistro(prontuario.getDataRegistro())
            .tipo(prontuario.getTipo())
            .titulo(prontuario.getTitulo())
            .conteudo(prontuario.getConteudo())
            .queixaPrincipal(prontuario.getQueixaPrincipal())
            .objetivoTerapeutico(prontuario.getObjetivoTerapeutico())
            .historico(prontuario.getHistorico())
            .evolucao(prontuario.getEvolucao())
            .planoTerapeutico(prontuario.getPlanoTerapeutico())
            .privado(prontuario.getPrivado())
            .status(prontuario.getStatus())
            .build();
    }
}

