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
    private String psicologLogin; // Identificador interno (opcional)
    private String nome;
    private String crp; // Número do CRP
    private String email;
    private String telefone;
    private LocalDate dtAtivacao;
    private Long categoriaId;
    private String categoriaNome;

    public static PsicologoResponse fromEntity(Psicologo psicologo) {
        return PsicologoResponse.builder()
                .id(psicologo.getId())
                .psicologLogin(psicologo.getPsicologLogin())
                .nome(psicologo.getNome())
                .crp(psicologo.getCrp())
                .email(psicologo.getEmail())
                .telefone(psicologo.getTelefone())
                .dtAtivacao(psicologo.getDtAtivacao())
                .categoriaId(psicologo.getCategoria() != null ? psicologo.getCategoria().getId() : null)
                .categoriaNome(psicologo.getCategoria() != null ? psicologo.getCategoria().getNome() : null)
                .build();
    }
}

