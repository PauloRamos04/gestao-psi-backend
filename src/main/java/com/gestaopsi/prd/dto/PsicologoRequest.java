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
    
    // Documentos
    @Size(max = 14)
    private String cpf;
    
    @Size(max = 20)
    private String rg;
    
    @Size(max = 20)
    private String crp; // Número do CRP
    
    // Contato
    @Email
    private String email;
    
    @Size(max = 20)
    private String telefone;
    
    @Size(max = 20)
    private String celular;
    
    @Size(max = 20)
    private String telefoneEmergencia;
    
    @Size(max = 100)
    private String contatoEmergenciaNome;
    
    // Dados Pessoais
    private LocalDate dataNascimento;
    
    @Size(max = 20)
    private String genero;
    
    @Size(max = 20)
    private String estadoCivil;
    
    @Size(max = 50)
    private String nacionalidade;
    
    // Endereço
    @Size(max = 10)
    private String cep;
    
    @Size(max = 200)
    private String logradouro;
    
    @Size(max = 10)
    private String numeroEndereco;
    
    @Size(max = 100)
    private String complemento;
    
    @Size(max = 100)
    private String bairro;
    
    @Size(max = 100)
    private String cidade;
    
    @Size(max = 2)
    private String estado;
    
    // Formação
    private String formacaoAcademica;
    
    private String especializacoes;
    
    @Size(max = 500)
    private String abordagemTerapeutica;
    
    @Size(max = 500)
    private String areasAtuacao;
    
    private Integer anosExperiencia;
    
    @Size(max = 200)
    private String universidadeFormacao;
    
    private Integer anoFormacao;
    
    // Profissional
    private LocalDate dtAtivacao;
    
    private Integer duracaoSessaoMinutos;
    
    private Boolean aceitaConvenio;
    
    @Size(max = 500)
    private String conveniosAceitos;
    
    private String observacoes;
    
    private String bio;
    
    private String fotoUrl;
    
    private Boolean ativo;
    
    @NotNull(message = "ID da categoria é obrigatório")
    private Long categoriaId;
    
    // Dados para criar usuário automaticamente (opcional)
    private String username; // Se fornecido, cria usuário automaticamente
    private String senha; // Senha do usuário (se criar usuário)
    private Long tipoUserId; // Tipo do usuário (se criar usuário)
}

