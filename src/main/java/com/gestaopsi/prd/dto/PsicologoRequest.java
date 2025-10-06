package com.gestaopsi.prd.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PsicologoRequest {
    
    @NotBlank(message = "Login do psicólogo é obrigatório")
    private String psicologLogin;
    
    @NotBlank(message = "Nome do psicólogo é obrigatório")
    private String nome;
    
    private LocalDate dtAtivacao;
    
    @NotNull(message = "ID da categoria é obrigatório")
    private Long categoriaId;
}

