package com.gestaopsi.prd.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioPacientesDTO {
    private Long totalPacientes;
    private Long pacientesAtivos;
    private Long pacientesInativos;
    private Double percentualAtivos;
}

