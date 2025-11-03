package com.gestaopsi.prd.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para requisição de login.
 * 
 * Ordem recomendada dos campos no formulário:
 * 1. clinicaLogin (Login da clínica - primeiro campo)
 * 2. username (Nome de usuário - segundo campo)
 * 3. password (Senha - terceiro campo)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    
    /**
     * Login da clínica (campo obrigatório - deve aparecer primeiro no formulário)
     */
    @NotBlank(message = "Login da clínica é obrigatório")
    private String clinicaLogin;

    /**
     * Nome de usuário (campo obrigatório - deve aparecer no meio do formulário)
     */
    @NotBlank(message = "Username é obrigatório")
    private String username;

    /**
     * Senha do usuário (campo obrigatório - deve aparecer por último no formulário)
     */
    @NotBlank(message = "Senha é obrigatória")
    private String password;
}


