-- Criação da tabela de permissões
CREATE TABLE IF NOT EXISTS permissions (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) UNIQUE NOT NULL,
    descricao VARCHAR(200),
    modulo VARCHAR(50),
    acao VARCHAR(50),
    ativo BOOLEAN NOT NULL DEFAULT TRUE
);

-- Atualiza tabela roles para adicionar novos campos
ALTER TABLE roles ADD COLUMN IF NOT EXISTS descricao VARCHAR(200);
ALTER TABLE roles ADD COLUMN IF NOT EXISTS ativo BOOLEAN NOT NULL DEFAULT TRUE;
ALTER TABLE roles ADD COLUMN IF NOT EXISTS sistema BOOLEAN NOT NULL DEFAULT FALSE;

-- Tabela de relacionamento entre roles e permissions
CREATE TABLE IF NOT EXISTS role_permissions (
    role_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    PRIMARY KEY (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES permissions(id) ON DELETE CASCADE
);

-- Índices para melhorar performance
CREATE INDEX IF NOT EXISTS idx_permissions_modulo ON permissions(modulo);
CREATE INDEX IF NOT EXISTS idx_permissions_acao ON permissions(acao);
CREATE INDEX IF NOT EXISTS idx_permissions_ativo ON permissions(ativo);
CREATE INDEX IF NOT EXISTS idx_roles_ativo ON roles(ativo);
CREATE INDEX IF NOT EXISTS idx_role_permissions_role ON role_permissions(role_id);
CREATE INDEX IF NOT EXISTS idx_role_permissions_permission ON role_permissions(permission_id);

-- Inserir permissões padrão
INSERT INTO permissions (nome, descricao, modulo, acao, ativo) VALUES
-- Usuários
('usuarios.criar', 'Criar novos usuários', 'usuarios', 'criar', TRUE),
('usuarios.ler', 'Visualizar usuários', 'usuarios', 'ler', TRUE),
('usuarios.editar', 'Editar usuários existentes', 'usuarios', 'editar', TRUE),
('usuarios.deletar', 'Deletar usuários', 'usuarios', 'deletar', TRUE),
('usuarios.exportar', 'Exportar lista de usuários', 'usuarios', 'exportar', TRUE),

-- Pacientes
('pacientes.criar', 'Criar novos pacientes', 'pacientes', 'criar', TRUE),
('pacientes.ler', 'Visualizar pacientes', 'pacientes', 'ler', TRUE),
('pacientes.editar', 'Editar pacientes existentes', 'pacientes', 'editar', TRUE),
('pacientes.deletar', 'Deletar pacientes', 'pacientes', 'deletar', TRUE),
('pacientes.exportar', 'Exportar lista de pacientes', 'pacientes', 'exportar', TRUE),

-- Sessões
('sessoes.criar', 'Criar novas sessões', 'sessoes', 'criar', TRUE),
('sessoes.ler', 'Visualizar sessões', 'sessoes', 'ler', TRUE),
('sessoes.editar', 'Editar sessões existentes', 'sessoes', 'editar', TRUE),
('sessoes.deletar', 'Deletar sessões', 'sessoes', 'deletar', TRUE),
('sessoes.exportar', 'Exportar lista de sessões', 'sessoes', 'exportar', TRUE),

-- Pagamentos
('pagamentos.criar', 'Criar novos pagamentos', 'pagamentos', 'criar', TRUE),
('pagamentos.ler', 'Visualizar pagamentos', 'pagamentos', 'ler', TRUE),
('pagamentos.editar', 'Editar pagamentos existentes', 'pagamentos', 'editar', TRUE),
('pagamentos.deletar', 'Deletar pagamentos', 'pagamentos', 'deletar', TRUE),
('pagamentos.exportar', 'Exportar lista de pagamentos', 'pagamentos', 'exportar', TRUE),

-- Prontuários
('prontuarios.criar', 'Criar novos prontuários', 'prontuarios', 'criar', TRUE),
('prontuarios.ler', 'Visualizar prontuários', 'prontuarios', 'ler', TRUE),
('prontuarios.editar', 'Editar prontuários existentes', 'prontuarios', 'editar', TRUE),
('prontuarios.deletar', 'Deletar prontuários', 'prontuarios', 'deletar', TRUE),
('prontuarios.exportar', 'Exportar prontuários', 'prontuarios', 'exportar', TRUE),

-- Clínicas
('clinicas.criar', 'Criar novas clínicas', 'clinicas', 'criar', TRUE),
('clinicas.ler', 'Visualizar clínicas', 'clinicas', 'ler', TRUE),
('clinicas.editar', 'Editar clínicas existentes', 'clinicas', 'editar', TRUE),
('clinicas.deletar', 'Deletar clínicas', 'clinicas', 'deletar', TRUE),

-- Psicólogos
('psicologos.criar', 'Criar novos psicólogos', 'psicologos', 'criar', TRUE),
('psicologos.ler', 'Visualizar psicólogos', 'psicologos', 'ler', TRUE),
('psicologos.editar', 'Editar psicólogos existentes', 'psicologos', 'editar', TRUE),
('psicologos.deletar', 'Deletar psicólogos', 'psicologos', 'deletar', TRUE),

-- Salas
('salas.criar', 'Criar novas salas', 'salas', 'criar', TRUE),
('salas.ler', 'Visualizar salas', 'salas', 'ler', TRUE),
('salas.editar', 'Editar salas existentes', 'salas', 'editar', TRUE),
('salas.deletar', 'Deletar salas', 'salas', 'deletar', TRUE),

-- Mensagens
('mensagens.criar', 'Criar novas mensagens', 'mensagens', 'criar', TRUE),
('mensagens.ler', 'Visualizar mensagens', 'mensagens', 'ler', TRUE),
('mensagens.editar', 'Editar mensagens existentes', 'mensagens', 'editar', TRUE),
('mensagens.deletar', 'Deletar mensagens', 'mensagens', 'deletar', TRUE),

-- Relatórios
('relatorios.financeiro', 'Visualizar relatórios financeiros', 'relatorios', 'ler', TRUE),
('relatorios.sessoes', 'Visualizar relatórios de sessões', 'relatorios', 'ler', TRUE),
('relatorios.pacientes', 'Visualizar relatórios de pacientes', 'relatorios', 'ler', TRUE),
('relatorios.exportar', 'Exportar relatórios', 'relatorios', 'exportar', TRUE),

-- Configurações do Sistema
('sistema.configurar', 'Acessar configurações do sistema', 'sistema', 'configurar', TRUE),
('sistema.logs', 'Visualizar logs do sistema', 'sistema', 'ler', TRUE),
('sistema.backup', 'Realizar backup do sistema', 'sistema', 'backup', TRUE),

-- Permissões e Roles
('permissoes.criar', 'Criar novas permissões', 'permissoes', 'criar', TRUE),
('permissoes.ler', 'Visualizar permissões', 'permissoes', 'ler', TRUE),
('permissoes.editar', 'Editar permissões existentes', 'permissoes', 'editar', TRUE),
('permissoes.deletar', 'Deletar permissões', 'permissoes', 'deletar', TRUE),

('roles.criar', 'Criar novas roles', 'roles', 'criar', TRUE),
('roles.ler', 'Visualizar roles', 'roles', 'ler', TRUE),
('roles.editar', 'Editar roles existentes', 'roles', 'editar', TRUE),
('roles.deletar', 'Deletar roles', 'roles', 'deletar', TRUE)
ON CONFLICT (nome) DO NOTHING;

-- Atualizar roles existentes como roles do sistema
UPDATE roles SET sistema = TRUE WHERE nome IN ('ADMIN', 'PSICOLOGO', 'FUNCIONARIO');
UPDATE roles SET descricao = 'Acesso completo ao sistema' WHERE nome = 'ADMIN';
UPDATE roles SET descricao = 'Acesso de psicólogo' WHERE nome = 'PSICOLOGO';
UPDATE roles SET descricao = 'Acesso de funcionário' WHERE nome = 'FUNCIONARIO';

-- Atribuir TODAS as permissões para role ADMIN
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.nome = 'ADMIN'
ON CONFLICT DO NOTHING;

-- Atribuir permissões básicas para role PSICOLOGO
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.nome = 'PSICOLOGO'
AND p.modulo IN ('pacientes', 'sessoes', 'prontuarios', 'pagamentos', 'relatorios')
AND p.acao IN ('criar', 'ler', 'editar')
ON CONFLICT DO NOTHING;

-- Atribuir permissões básicas para role FUNCIONARIO
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.nome = 'FUNCIONARIO'
AND p.modulo IN ('pacientes', 'sessoes', 'pagamentos')
AND p.acao IN ('ler', 'criar')
ON CONFLICT DO NOTHING;


