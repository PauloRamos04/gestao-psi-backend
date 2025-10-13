-- Migration definitiva com todo o schema do sistema
-- Criada em: 13/10/2025

-- ========================================
-- TABELAS PRINCIPAIS
-- ========================================

-- Tipos de Usuário
CREATE TABLE IF NOT EXISTS tipos_usuario (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(50) NOT NULL UNIQUE,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tipos de Pagamento
CREATE TABLE IF NOT EXISTS tipos_pagamento (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(50) NOT NULL UNIQUE,
    descricao VARCHAR(255),
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Categorias
CREATE TABLE IF NOT EXISTS categorias (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Clínicas
CREATE TABLE IF NOT EXISTS clinicas (
    id BIGSERIAL PRIMARY KEY,
    clinica_login VARCHAR(50) NOT NULL UNIQUE,
    nome VARCHAR(255) NOT NULL,
    titulo VARCHAR(255),
    status BOOLEAN DEFAULT true,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Psicólogos
CREATE TABLE IF NOT EXISTS psicologos (
    id BIGSERIAL PRIMARY KEY,
    psicolog_login VARCHAR(50) NOT NULL UNIQUE,
    nome VARCHAR(255) NOT NULL,
    crp VARCHAR(20) UNIQUE,
    email VARCHAR(255),
    telefone VARCHAR(20),
    dt_ativacao DATE,
    categoria_id BIGINT REFERENCES categorias(id),
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Usuários
CREATE TABLE IF NOT EXISTS usuarios (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    clinica_id BIGINT NOT NULL REFERENCES clinicas(id),
    psicologo_id BIGINT REFERENCES psicologos(id),
    tipo_id BIGINT REFERENCES tipos_usuario(id),
    nome_completo VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    telefone VARCHAR(20),
    cargo VARCHAR(100),
    status BOOLEAN DEFAULT true,
    tema_preferido VARCHAR(20) DEFAULT 'light',
    idioma VARCHAR(10) DEFAULT 'pt-BR',
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version INTEGER DEFAULT 0
);

-- Pacientes
CREATE TABLE IF NOT EXISTS pacientes (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    cpf VARCHAR(14) UNIQUE,
    email VARCHAR(255),
    telefone VARCHAR(20),
    data_nascimento DATE,
    endereco TEXT,
    cidade VARCHAR(100),
    estado VARCHAR(2),
    cep VARCHAR(10),
    clinica_id BIGINT NOT NULL REFERENCES clinicas(id),
    psicologo_id BIGINT NOT NULL REFERENCES psicologos(id),
    status BOOLEAN DEFAULT true,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version INTEGER DEFAULT 0
);

-- Salas
CREATE TABLE IF NOT EXISTS salas (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    numero VARCHAR(20),
    capacidade INTEGER DEFAULT 1,
    clinica_id BIGINT NOT NULL REFERENCES clinicas(id),
    ativa BOOLEAN DEFAULT true,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version INTEGER DEFAULT 0
);

-- Sessões
CREATE TABLE IF NOT EXISTS sessoes (
    id BIGSERIAL PRIMARY KEY,
    paciente_id BIGINT NOT NULL REFERENCES pacientes(id),
    psicologo_id BIGINT NOT NULL REFERENCES psicologos(id),
    sala_id BIGINT REFERENCES salas(id),
    data DATE NOT NULL,
    hora TIME NOT NULL,
    duracao INTEGER DEFAULT 50,
    status BOOLEAN DEFAULT true,
    observacoes TEXT,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version INTEGER DEFAULT 0
);

-- Pagamentos
CREATE TABLE IF NOT EXISTS pagamentos (
    id BIGSERIAL PRIMARY KEY,
    paciente_id BIGINT NOT NULL REFERENCES pacientes(id),
    psicologo_id BIGINT NOT NULL REFERENCES psicologos(id),
    tipo_pagamento_id BIGINT REFERENCES tipos_pagamento(id),
    valor DECIMAL(10,2) NOT NULL,
    data_pagamento DATE NOT NULL,
    observacoes TEXT,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version INTEGER DEFAULT 0
);

-- Prontuários
CREATE TABLE IF NOT EXISTS prontuarios (
    id BIGSERIAL PRIMARY KEY,
    paciente_id BIGINT NOT NULL REFERENCES pacientes(id),
    psicologo_id BIGINT NOT NULL REFERENCES psicologos(id),
    data_atendimento DATE NOT NULL,
    queixa_principal TEXT,
    historico TEXT,
    observacoes TEXT,
    plano_terapeutico TEXT,
    evolucao TEXT,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version INTEGER DEFAULT 0
);

-- Mensagens
CREATE TABLE IF NOT EXISTS mensagens (
    id BIGSERIAL PRIMARY KEY,
    remetente_id BIGINT REFERENCES usuarios(id),
    destinatario_id BIGINT REFERENCES usuarios(id),
    assunto VARCHAR(255),
    conteudo TEXT NOT NULL,
    lida BOOLEAN DEFAULT false,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version INTEGER DEFAULT 0
);

-- Notificações
CREATE TABLE IF NOT EXISTS notificacoes (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL REFERENCES usuarios(id),
    titulo VARCHAR(255) NOT NULL,
    mensagem TEXT NOT NULL,
    tipo VARCHAR(50) DEFAULT 'info',
    lida BOOLEAN DEFAULT false,
    data_envio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version INTEGER DEFAULT 0
);

-- ========================================
-- SISTEMA DE PERMISSÕES RBAC
-- ========================================

-- Roles
CREATE TABLE IF NOT EXISTS roles (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(50) NOT NULL UNIQUE,
    descricao TEXT,
    ativo BOOLEAN DEFAULT true,
    sistema BOOLEAN DEFAULT false,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version INTEGER DEFAULT 0
);

-- Permissões
CREATE TABLE IF NOT EXISTS permissions (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL UNIQUE,
    modulo VARCHAR(50) NOT NULL,
    acao VARCHAR(50) NOT NULL,
    descricao TEXT,
    ativo BOOLEAN DEFAULT true,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version INTEGER DEFAULT 0
);

-- Relação Role-Permissão
CREATE TABLE IF NOT EXISTS role_permissions (
    role_id BIGINT NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
    permission_id BIGINT NOT NULL REFERENCES permissions(id) ON DELETE CASCADE,
    PRIMARY KEY (role_id, permission_id)
);

-- Relação Usuário-Role
CREATE TABLE IF NOT EXISTS usuario_roles (
    usuario_id BIGINT NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    role_id BIGINT NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
    PRIMARY KEY (usuario_id, role_id)
);

-- ========================================
-- CONFIGURAÇÕES DO SISTEMA
-- ========================================

-- Configurações do Sistema
CREATE TABLE IF NOT EXISTS system_config (
    id BIGSERIAL PRIMARY KEY,
    chave VARCHAR(100) NOT NULL UNIQUE,
    valor TEXT,
    descricao TEXT,
    tipo VARCHAR(50) DEFAULT 'string',
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version INTEGER DEFAULT 0
);

-- ========================================
-- TABELAS AUXILIARES
-- ========================================

-- Sublocações
CREATE TABLE IF NOT EXISTS sublocacoes (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    clinica_id BIGINT NOT NULL REFERENCES clinicas(id),
    ativa BOOLEAN DEFAULT true,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version INTEGER DEFAULT 0
);

-- Interações
CREATE TABLE IF NOT EXISTS interacoes (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL REFERENCES usuarios(id),
    acao VARCHAR(100) NOT NULL,
    entidade VARCHAR(100),
    entidade_id BIGINT,
    detalhes TEXT,
    ip_address VARCHAR(45),
    user_agent TEXT,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Download Requests
CREATE TABLE IF NOT EXISTS download_requests (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL REFERENCES usuarios(id),
    tipo VARCHAR(50) NOT NULL,
    filtros TEXT,
    status VARCHAR(20) DEFAULT 'pending',
    arquivo_path VARCHAR(500),
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    concluido_em TIMESTAMP
);

-- ========================================
-- ÍNDICES PARA PERFORMANCE
-- ========================================

-- Índices para usuários
CREATE INDEX IF NOT EXISTS idx_usuarios_username ON usuarios(username);
CREATE INDEX IF NOT EXISTS idx_usuarios_clinica ON usuarios(clinica_id);
CREATE INDEX IF NOT EXISTS idx_usuarios_psicologo ON usuarios(psicologo_id);

-- Índices para pacientes
CREATE INDEX IF NOT EXISTS idx_pacientes_cpf ON pacientes(cpf);
CREATE INDEX IF NOT EXISTS idx_pacientes_clinica ON pacientes(clinica_id);
CREATE INDEX IF NOT EXISTS idx_pacientes_psicologo ON pacientes(psicologo_id);

-- Índices para sessões
CREATE INDEX IF NOT EXISTS idx_sessoes_data ON sessoes(data);
CREATE INDEX IF NOT EXISTS idx_sessoes_paciente ON sessoes(paciente_id);
CREATE INDEX IF NOT EXISTS idx_sessoes_psicologo ON sessoes(psicologo_id);

-- Índices para mensagens
CREATE INDEX IF NOT EXISTS idx_mensagens_remetente ON mensagens(remetente_id);
CREATE INDEX IF NOT EXISTS idx_mensagens_destinatario ON mensagens(destinatario_id);
CREATE INDEX IF NOT EXISTS idx_mensagens_lida ON mensagens(lida);

-- Índices para notificações
CREATE INDEX IF NOT EXISTS idx_notificacoes_usuario ON notificacoes(usuario_id);
CREATE INDEX IF NOT EXISTS idx_notificacoes_lida ON notificacoes(lida);

-- Índices para permissões
CREATE INDEX IF NOT EXISTS idx_permissions_modulo ON permissions(modulo);
CREATE INDEX IF NOT EXISTS idx_permissions_acao ON permissions(acao);
CREATE INDEX IF NOT EXISTS idx_permissions_ativo ON permissions(ativo);

-- Índices para configurações
CREATE INDEX IF NOT EXISTS idx_system_config_chave ON system_config(chave);

-- ========================================
-- COMENTÁRIOS NAS TABELAS
-- ========================================

COMMENT ON TABLE usuarios IS 'Tabela de usuários do sistema';
COMMENT ON TABLE clinicas IS 'Tabela de clínicas/tenants';
COMMENT ON TABLE psicologos IS 'Tabela de psicólogos';
COMMENT ON TABLE pacientes IS 'Tabela de pacientes';
COMMENT ON TABLE sessoes IS 'Tabela de sessões de terapia';
COMMENT ON TABLE pagamentos IS 'Tabela de pagamentos';
COMMENT ON TABLE prontuarios IS 'Tabela de prontuários médicos';
COMMENT ON TABLE roles IS 'Tabela de roles do sistema RBAC';
COMMENT ON TABLE permissions IS 'Tabela de permissões do sistema RBAC';
COMMENT ON TABLE system_config IS 'Tabela de configurações do sistema';
