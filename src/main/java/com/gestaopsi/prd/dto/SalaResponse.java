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
    private Integer clinicaId;
    private String nome;
    private String clinicaNome;

    public static SalaResponse fromEntity(Sala sala) {
        return SalaResponse.builder()
                .id(sala.getId())
                .clinicaId(sala.getClinica() != null ? sala.getClinica().getId().intValue() : null)
                .nome(sala.getNome())
                .clinicaNome(sala.getClinica() != null ? sala.getClinica().getNome() : null)
                .build();
    }
}

