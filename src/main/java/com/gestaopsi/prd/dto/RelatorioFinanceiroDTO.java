package com.gestaopsi.prd.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioFinanceiroDTO {
    private Double totalRecebido;
    private Long quantidadePagamentos;
    private Double ticketMedio;
    private Map<String, Double> porTipoPagamento;
    private Map<String, Double> porMes;
}

