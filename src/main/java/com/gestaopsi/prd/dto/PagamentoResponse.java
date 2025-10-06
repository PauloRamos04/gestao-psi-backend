package com.gestaopsi.prd.dto;

import com.gestaopsi.prd.entity.Pagamento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagamentoResponse {
    private Long id;
    private Integer clinicaId;
    private Integer psicologId;
    private Long pacienteId;
    private Long sessaoId;
    private Double valor;
    private LocalDate data;
    private Integer tipoPagamentoId;
    private String observacoes;
    
    // Dados relacionados
    private String clinicaNome;
    private String psicologoNome;
    private String pacienteNome;
    private String tipoPagamentoNome;

    public static PagamentoResponse fromEntity(Pagamento pagamento) {
        return PagamentoResponse.builder()
                .id(pagamento.getId())
                .clinicaId(pagamento.getClinica() != null ? pagamento.getClinica().getId().intValue() : null)
                .psicologId(pagamento.getPsicologo() != null ? pagamento.getPsicologo().getId().intValue() : null)
                .pacienteId(pagamento.getPaciente() != null ? pagamento.getPaciente().getId() : null)
                .sessaoId(null) // Pagamento não tem relacionamento com Sessao nesta versão
                .valor(pagamento.getValor() != null ? pagamento.getValor().doubleValue() : 0.0)
                .data(pagamento.getData())
                .tipoPagamentoId(pagamento.getTipoPagamento() != null ? pagamento.getTipoPagamento().getId().intValue() : null)
                .observacoes(pagamento.getObservacoes())
                .clinicaNome(pagamento.getClinica() != null ? pagamento.getClinica().getNome() : null)
                .psicologoNome(pagamento.getPsicologo() != null ? pagamento.getPsicologo().getNome() : null)
                .pacienteNome(pagamento.getPaciente() != null ? pagamento.getPaciente().getNome() : null)
                .tipoPagamentoNome(pagamento.getTipoPagamento() != null ? pagamento.getTipoPagamento().getNome() : null)
                .build();
    }
}

