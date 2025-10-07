package com.gestaopsi.prd.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagamentoRequest {
    
    @NotNull(message = "ID da clínica é obrigatório")
    private Long clinicaId;
    
    @NotNull(message = "ID do psicólogo é obrigatório")
    private Long psicologId;
    
    @NotNull(message = "ID do paciente é obrigatório")
    private Long pacienteId;
    
    private Long sessaoId;
    
    @NotNull(message = "Valor é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
    private BigDecimal valor;
    
    @NotNull(message = "Data é obrigatória")
    private LocalDate data;
    
    @NotNull(message = "Tipo de pagamento é obrigatório")
    private Long tipoPagamentoId;
    
    private String observacoes;

    // Campos de Convênio
    private Boolean ehConvenio;
    
    private String convenio;
    
    private String numeroGuia;
    
    private BigDecimal valorConvenio;
    
    private BigDecimal valorCoparticipacao;
}

