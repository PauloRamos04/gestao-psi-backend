package com.gestaopsi.prd.dto;

import com.gestaopsi.prd.entity.Psicologo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PsicologoResponse {
    private Long id;
    private String psicologLogin;
    private String nome;
    private LocalDate dtAtivacao;
    private Integer categoriaId;
    private String categoriaNome;

    public static PsicologoResponse fromEntity(Psicologo psicologo) {
        return PsicologoResponse.builder()
                .id(psicologo.getId())
                .psicologLogin(psicologo.getPsicologLogin())
                .nome(psicologo.getNome())
                .dtAtivacao(psicologo.getDtAtivacao())
                .categoriaId(psicologo.getCategoria() != null ? psicologo.getCategoria().getId().intValue() : null)
                .categoriaNome(psicologo.getCategoria() != null ? psicologo.getCategoria().getNome() : null)
                .build();
    }
}

