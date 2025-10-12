package com.gestaopsi.prd.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemConfigRequest {
    
    @NotBlank(message = "Categoria é obrigatória")
    private String category;
    
    @NotNull(message = "Configurações não podem ser nulas")
    private Map<String, Object> configs;
}

