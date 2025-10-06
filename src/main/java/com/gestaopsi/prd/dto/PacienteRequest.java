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
public class PacienteRequest {
    
    @NotNull(message = "ID da clínica é obrigatório")
    private Long clinicaId;
    
    @NotNull(message = "ID do psicólogo é obrigatório")
    private Long psicologId;
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 200, message = "Nome deve ter entre 3 e 200 caracteres")
    private String nome;
    
    private String cpf;
    
    @Email(message = "Email inválido")
    private String email;
    
    private String telefone;
    
    @Past(message = "Data de nascimento deve estar no passado")
    private LocalDate dataNascimento;
    
    private String endereco;
    
    private Integer generoId;
}

