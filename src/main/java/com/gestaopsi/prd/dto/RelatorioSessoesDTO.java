package com.gestaopsi.prd.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioSessoesDTO {
    private Long totalSessoes;
    private Long sessoesConfirmadas;
    private Long sessoesPendentes;
    private Double taxaConfirmacao;
}

