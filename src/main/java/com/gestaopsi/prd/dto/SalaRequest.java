package com.gestaopsi.prd.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para requisição de criação/atualização de sala.
 * 
 * IMPORTANTE: O campo clinicaId é obrigatório e deve ser fornecido pelo frontend.
 * O frontend deve permitir a seleção da clínica no formulário de cadastro de salas.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalaRequest {
    
    /**
     * ID da clínica (OBRIGATÓRIO - deve ser selecionado no formulário de cadastro)
     * Use o endpoint GET /clinicas para obter a lista de clínicas disponíveis
     */
    @NotNull(message = "ID da clínica é obrigatório")
    private Long clinicaId;
    
    private Long psicologoResponsavelId; // Psicólogo responsável/dono da sala (opcional)
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;
    
    @Size(max = 20)
    private String numero; // Ex: "101", "Sala A"
    
    @Size(max = 500)
    private String descricao;
    
    @Min(1)
    @Max(50)
    private Integer capacidade; // Número de pessoas
    
    private Boolean ativa; // Sala ativa/inativa
    
    @Pattern(regexp = "^#([A-Fa-f0-9]{6})$", message = "Cor deve estar no formato hex (#RRGGBB)")
    private String cor; // Cor para calendário
    
    @Min(0)
    @Max(100)
    private Integer andar; // Andar do prédio
    
    @Size(max = 50)
    private String bloco; // Bloco/Ala
    
    @Size(max = 500)
    private String observacoes;
    
    private Boolean exclusiva; // Se true, apenas psicólogo responsável pode usar
    
    private Boolean permiteCompartilhamento; // Permite compartilhamento com outros psicólogos
}

