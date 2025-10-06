package com.gestaopsi.prd.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProntuarioRequest {
    
    @NotNull(message = "ID do paciente é obrigatório")
    private Long pacienteId;
    
    private Long sessaoId;
    
    @NotNull(message = "ID do psicólogo é obrigatório")
    private Long psicologId;
    
    @NotBlank(message = "Tipo é obrigatório")
    private String tipo; // ANAMNESE, EVOLUCAO, OBSERVACAO
    
    @Size(max = 200, message = "Título deve ter no máximo 200 caracteres")
    private String titulo;
    
    @NotBlank(message = "Conteúdo é obrigatório")
    private String conteudo;
    
    @Size(max = 500)
    private String queixaPrincipal;
    
    @Size(max = 500)
    private String objetivoTerapeutico;
    
    private String historico;
    
    private String evolucao;
    
    private String planoTerapeutico;
    
    private Boolean privado = true;
}

