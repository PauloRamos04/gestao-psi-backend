package com.gestaopsi.prd.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "psicologos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Psicologo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "psicolog_login", unique = true, length = 50)
    private String psicologLogin; // Identificador interno (opcional)

    @Column(name = "nome", nullable = false, length = 200)
    private String nome;

    // Documentos
    @Column(name = "cpf", unique = true, length = 14)
    private String cpf;

    @Column(name = "rg", length = 20)
    private String rg;

    @Column(name = "crp", length = 20)
    private String crp; // Número do CRP (Conselho Regional de Psicologia)

    // Contato
    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "telefone", length = 20)
    private String telefone;

    @Column(name = "celular", length = 20)
    private String celular;

    @Column(name = "telefone_emergencia", length = 20)
    private String telefoneEmergencia;

    @Column(name = "contato_emergencia_nome", length = 100)
    private String contatoEmergenciaNome;

    // Dados Pessoais
    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(name = "genero", length = 20)
    private String genero; // MASCULINO, FEMININO, OUTRO, PREFIRO_NAO_DIZER

    @Column(name = "estado_civil", length = 20)
    private String estadoCivil; // SOLTEIRO, CASADO, DIVORCIADO, VIUVO, UNIAO_ESTAVEL

    @Column(name = "nacionalidade", length = 50)
    private String nacionalidade;

    // Endereço
    @Column(name = "cep", length = 10)
    private String cep;

    @Column(name = "logradouro", length = 200)
    private String logradouro;

    @Column(name = "numero_endereco", length = 10)
    private String numeroEndereco;

    @Column(name = "complemento", length = 100)
    private String complemento;

    @Column(name = "bairro", length = 100)
    private String bairro;

    @Column(name = "cidade", length = 100)
    private String cidade;

    @Column(name = "estado", length = 2)
    private String estado; // UF

    // Formação e Experiência Profissional
    @Column(name = "formacao_academica", columnDefinition = "TEXT")
    private String formacaoAcademica;

    @Column(name = "especializacoes", columnDefinition = "TEXT")
    private String especializacoes;

    @Column(name = "abordagem_terapeutica", length = 500)
    private String abordagemTerapeutica; // Ex: TCC, Psicanálise, Gestalt, etc.

    @Column(name = "areas_atuacao", length = 500)
    private String areasAtuacao; // Ex: Ansiedade, Depressão, Relacionamentos, etc.

    @Column(name = "anos_experiencia")
    private Integer anosExperiencia;

    @Column(name = "universidade_formacao", length = 200)
    private String universidadeFormacao;

    @Column(name = "ano_formacao")
    private Integer anoFormacao;

    // Profissional
    @Column(name = "dt_ativacao")
    private LocalDate dtAtivacao;

    @Column(name = "valor_consulta")
    private java.math.BigDecimal valorConsulta;

    @Column(name = "duracao_sessao_minutos")
    private Integer duracaoSessaoMinutos; // Duração padrão da sessão

    @Column(name = "aceita_convenio")
    @Builder.Default
    private Boolean aceitaConvenio = false;

    @Column(name = "convenios_aceitos", length = 500)
    private String conveniosAceitos;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio; // Biografia para apresentação

    @Column(name = "foto_url", length = 500)
    private String fotoUrl;

    @Column(name = "ativo")
    @Builder.Default
    private Boolean ativo = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    // Compatibilidade
    @Transient
    public Integer getCategoriaId() {
        return categoria != null ? categoria.getId().intValue() : null;
    }
}
