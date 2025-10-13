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
public class UsuarioUpdateRequest {
    
    @NotBlank(message = "Username é obrigatório")
    private String username;
    
    @NotNull(message = "ID da clínica é obrigatório")
    private Long clinicaId;
    
    @NotNull(message = "ID do psicólogo é obrigatório")
    private Long psicologId;
    
    // tipoId agora é opcional - sistema migra para roles
    private Long tipoId;
    
    // Senha é opcional na atualização
    private String senha;
    
    @NotBlank(message = "Nome completo é obrigatório")
    private String titulo;
    
    @Builder.Default
    private Boolean status = true;
    
    private Long roleId;
    
    // Campos adicionais para informações pessoais
    private String nomeCompleto;
    private String email;
    private String telefone;
    private String celular;
    private String cargo;
    private String departamento;
    private String fotoUrl;
    private String observacoes;
    
    // Preferências
    private String temaPreferido;
    private String idioma;
    private String timezone;
    private Boolean receberNotificacoesEmail;
    private Boolean receberNotificacoesSistema;
    private Boolean lembretesSessao;
    private Boolean notificacoesPagamento;
}


