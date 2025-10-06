package com.gestaopsi.prd.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClinicaRequest {
    
    @NotBlank(message = "Login da clínica é obrigatório")
    private String clinicaLogin;
    
    @NotBlank(message = "Nome da clínica é obrigatório")
    private String nome;
    
    private String titulo;
    
    @Builder.Default
    private Boolean status = true;
}
