-- Remover índices da V18 que podem ter falhado
DROP INDEX IF EXISTS idx_pagamentos_sessao;
DROP INDEX IF EXISTS idx_pagamentos_data_metodo;

-- Índices para otimização de performance (corrigidos)

-- Pacientes: busca por clinica + psicologo + status
CREATE INDEX IF NOT EXISTS idx_pacientes_clinica_psic_status 
ON pacientes(clinica_id, psicolog_id, status);

-- Pacientes: busca por CPF
CREATE INDEX IF NOT EXISTS idx_pacientes_cpf 
ON pacientes(cpf) WHERE cpf IS NOT NULL;

-- Sessões: busca por data
CREATE INDEX IF NOT EXISTS idx_sessoes_data 
ON sessoes(data);

-- Sessões: busca por data + sala
CREATE INDEX IF NOT EXISTS idx_sessoes_data_sala 
ON sessoes(data, sala_id);

-- Sessões: busca por psicólogo + data
CREATE INDEX IF NOT EXISTS idx_sessoes_psicologo_data 
ON sessoes(psicolog_id, data);

-- Sessões: busca por paciente
CREATE INDEX IF NOT EXISTS idx_sessoes_paciente 
ON sessoes(paciente_id);

-- Pagamentos: busca por data
CREATE INDEX IF NOT EXISTS idx_pagamentos_data 
ON pagamentos(data);

-- Pagamentos: busca por data + tipo
CREATE INDEX IF NOT EXISTS idx_pagamentos_data_tipo 
ON pagamentos(data, tipo_pagamento_id);

-- Pagamentos: busca por paciente
CREATE INDEX IF NOT EXISTS idx_pagamentos_paciente 
ON pagamentos(paciente_id);

-- Usuários: busca por username
CREATE INDEX IF NOT EXISTS idx_usuarios_username 
ON usuarios(username);

-- Notificações: busca por usuário + lida
CREATE INDEX IF NOT EXISTS idx_notificacoes_usuario_lida 
ON notificacoes(usuario_id, lida);

-- Logs: busca por data + ação
CREATE INDEX IF NOT EXISTS idx_logs_data_acao 
ON logs_auditoria(data_hora, acao);

-- Logs: busca por usuário
CREATE INDEX IF NOT EXISTS idx_logs_usuario 
ON logs_auditoria(usuario_id);

-- Mensagens: busca por tipo + ativa
CREATE INDEX IF NOT EXISTS idx_mensagens_tipo_ativa 
ON mensagens(tipo, ativa);

-- Download Requests: busca por status
CREATE INDEX IF NOT EXISTS idx_download_requests_status 
ON download_requests(status);

-- Download Requests: busca por usuário + data
CREATE INDEX IF NOT EXISTS idx_download_requests_usuario_data 
ON download_requests(user_id, created_at);

