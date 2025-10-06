package com.gestaopsi.prd.dto;

import com.gestaopsi.prd.entity.Clinica;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClinicaResponse {
    private Long id;
    private String clinicaLogin;
    private String nome;
    private Boolean status;
    private String titulo;

    public static ClinicaResponse fromEntity(Clinica clinica) {
        return ClinicaResponse.builder()
                .id(clinica.getId())
                .clinicaLogin(clinica.getClinicaLogin())
                .nome(clinica.getNome())
                .status(clinica.getStatus())
                .titulo(clinica.getTitulo())
                .build();
    }
}

