package com.gestaopsi.prd.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "pacientes", indexes = {
    @Index(name = "idx_paciente_cpf", columnList = "cpf"),
    @Index(name = "idx_paciente_email", columnList = "email"),
    @Index(name = "idx_paciente_status", columnList = "status")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clinica_id", nullable = false)
    private Clinica clinica;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "psicolog_id", nullable = false)
    private Psicologo psicologo;

    @Column(name = "nome", nullable = false, length = 200)
    private String nome;

    @Column(name = "status", nullable = false)
    @Builder.Default
    private Boolean status = true;

    // Documentos
    @Column(name = "cpf", length = 14)
    private String cpf;

    @Column(name = "rg", length = 20)
    private String rg;

    @Column(name = "orgao_emissor_rg", length = 20)
    private String orgaoEmissorRg;

    // Dados Pessoais
    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(name = "idade")
    private Integer idade; // Calculado automaticamente

    @Column(name = "genero", length = 20)
    private String genero; // MASCULINO, FEMININO, OUTRO, PREFIRO_NAO_DIZER

    @Column(name = "estado_civil", length = 20)
    private String estadoCivil;

    @Column(name = "profissao", length = 100)
    private String profissao;

    @Column(name = "escolaridade", length = 50)
    private String escolaridade;

    @Column(name = "nacionalidade", length = 50)
    private String nacionalidade;

    @Column(name = "natural_de", length = 100)
    private String naturalDe; // Cidade natal

    // Contato
    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "telefone", length = 20)
    private String telefone;

    @Column(name = "celular", length = 20)
    private String celular;

    @Column(name = "telefone_recado", length = 20)
    private String telefoneRecado;

    @Column(name = "contato_recado_nome", length = 100)
    private String contatoRecadoNome;

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

    // Responsável (se menor de idade)
    @Column(name = "responsavel_nome", length = 200)
    private String responsavelNome;

    @Column(name = "responsavel_cpf", length = 14)
    private String responsavelCpf;

    @Column(name = "responsavel_parentesco", length = 50)
    private String responsavelParentesco; // PAI, MAE, TUTOR, OUTRO

    @Column(name = "responsavel_telefone", length = 20)
    private String responsavelTelefone;

    // Informações Clínicas
    @Column(name = "motivo_consulta", columnDefinition = "TEXT")
    private String motivoConsulta; // Por que busca terapia

    @Column(name = "queixa_principal", columnDefinition = "TEXT")
    private String queixaPrincipal;

    @Column(name = "historico_familiar", columnDefinition = "TEXT")
    private String historicoFamiliar;

    @Column(name = "medicamentos_uso", columnDefinition = "TEXT")
    private String medicamentosUso;

    @Column(name = "alergias", length = 500)
    private String alergias;

    @Column(name = "condicoes_medicas", columnDefinition = "TEXT")
    private String condicoesMedicas;

    @Column(name = "em_tratamento_psiquiatrico")
    @Builder.Default
    private Boolean emTratamentoPsiquiatrico = false;

    @Column(name = "psiquiatra_nome", length = 200)
    private String psiquiatraNome;

    @Column(name = "uso_medicacao_psiquiatrica")
    @Builder.Default
    private Boolean usoMedicacaoPsiquiatrica = false;

    // Informações Adicionais
    @Column(name = "como_conheceu", length = 200)
    private String comoConheceu; // Como conheceu a clínica

    @Column(name = "convenio_saude", length = 100)
    private String convenioSaude;

    @Column(name = "numero_carteirinha", length = 50)
    private String numeroCarteirinha;

    @Column(name = "plano", length = 100)
    private String plano;

    @Column(name = "data_primeira_consulta")
    private LocalDate dataPrimeiraConsulta;

    @Column(name = "data_ultima_consulta")
    private LocalDate dataUltimaConsulta;

    @Column(name = "numero_sessoes_realizadas")
    @Builder.Default
    private Integer numeroSessoesRealizadas = 0;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "foto_url", length = 500)
    private String fotoUrl;

    // Compatibilidade com código existente
    @Transient
    public Integer getClinicaId() {
        return clinica != null ? clinica.getId().intValue() : null;
    }

    @Transient
    public Integer getPsicologId() {
        return psicologo != null ? psicologo.getId().intValue() : null;
    }
}
