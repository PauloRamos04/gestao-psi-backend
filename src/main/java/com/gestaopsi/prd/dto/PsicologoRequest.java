package com.gestaopsi.prd.dto;

import jakarta.validation.constraints.*;
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
    
    private String psicologLogin; // Identificador interno (opcional)
    
    @NotBlank(message = "Nome do psicólogo é obrigatório")
    @Size(min = 3, max = 200)
    private String nome;
    
    @Size(max = 20)
    private String crp; // Número do CRP
    
    @Email
    private String email;
    
    @Size(max = 20)
    private String telefone;
    
    private LocalDate dtAtivacao;
    
    @NotNull(message = "ID da categoria é obrigatório")
    private Long categoriaId;
    
    // Dados para criar usuário automaticamente (opcional)
    private String username; // Se fornecido, cria usuário automaticamente
    private String senha; // Senha do usuário (se criar usuário)
    private Long tipoUserId; // Tipo do usuário (se criar usuário)
}

