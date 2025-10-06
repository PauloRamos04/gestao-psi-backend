package com.gestaopsi.prd.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessaoRequest {
    
    @NotNull(message = "ID da clínica é obrigatório")
    private Long clinicaId;
    
    @NotNull(message = "ID do psicólogo é obrigatório")
    private Long psicologId;
    
    @NotNull(message = "ID do paciente é obrigatório")
    private Long pacienteId;
    
    private Long salaId;
    
    @NotNull(message = "Data é obrigatória")
    @FutureOrPresent(message = "Data não pode estar no passado")
    private LocalDate data;
    
    @NotNull(message = "Hora é obrigatória")
    private LocalTime hora;
    
    private String observacoes;
}

