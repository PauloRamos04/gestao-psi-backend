package com.gestaopsi.prd.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRequest {
    
    @NotBlank(message = "Username é obrigatório")
    private String username;
    
    @NotNull(message = "ID da clínica é obrigatório")
    private Long clinicaId;
    
    @NotNull(message = "ID do psicólogo é obrigatório")
    private Long psicologId;
    
    @NotNull(message = "ID do tipo de usuário é obrigatório")
    private Long tipoId;
    
    @NotBlank(message = "Senha é obrigatória")
    private String senha;
    
    @NotBlank(message = "Nome completo é obrigatório")
    private String titulo;
    
    @Builder.Default
    private Boolean status = true;
    
    private Long roleId;
}
