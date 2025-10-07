package com.gestaopsi.prd.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "logs_auditoria", indexes = {
    @Index(name = "idx_logs_data", columnList = "dataHora"),
    @Index(name = "idx_logs_usuario", columnList = "usuarioId"),
    @Index(name = "idx_logs_entidade", columnList = "entidade"),
    @Index(name = "idx_logs_acao", columnList = "acao")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogAuditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    @Column(name = "usuario_id")
    private Long usuarioId;

    @Column(name = "username", length = 50)
    private String username;

    @Column(name = "acao", nullable = false, length = 20)
    private String acao; // CREATE, UPDATE, DELETE, LOGIN, LOGOUT, VIEW, EXPORT

    @Column(name = "entidade", length = 50)
    private String entidade; // Paciente, Sessao, Prontuario, etc.

    @Column(name = "entidade_id")
    private Long entidadeId;

    @Column(name = "descricao", length = 500)
    private String descricao; // Descrição da ação

    @Column(name = "dados_anteriores", columnDefinition = "TEXT")
    private String dadosAnteriores; // JSON com dados antes da alteração

    @Column(name = "dados_novos", columnDefinition = "TEXT")
    private String dadosNovos; // JSON com dados após alteração

    @Column(name = "ip_address", length = 45)
    private String ipAddress; // IPv4 ou IPv6

    @Column(name = "user_agent", length = 500)
    private String userAgent; // Navegador/dispositivo

    @Column(name = "metodo_http", length = 10)
    private String metodoHttp; // GET, POST, PUT, DELETE

    @Column(name = "endpoint", length = 200)
    private String endpoint; // URL do endpoint

    @Column(name = "status_code")
    private Integer statusCode; // HTTP status code

    @Column(name = "tempo_execucao_ms")
    private Long tempoExecucaoMs; // Tempo de execução em milissegundos

    @Column(name = "sucesso")
    @Builder.Default
    private Boolean sucesso = true;

    @Column(name = "mensagem_erro", columnDefinition = "TEXT")
    private String mensagemErro; // Se houve erro

    @Column(name = "clinica_id")
    private Long clinicaId;

    @Column(name = "psicologo_id")
    private Long psicologoId;

    @Column(name = "modulo", length = 50)
    private String modulo; // SESSOES, PRONTUARIOS, PAGAMENTOS, USUARIOS, etc.

    @Column(name = "nivel", length = 20)
    @Builder.Default
    private String nivel = "INFO"; // INFO, WARNING, ERROR, CRITICAL

    @Column(name = "tags", length = 500)
    private String tags; // Tags para busca/filtro
}

