-- Índices para otimização de performance
-- Melhora drasticamente consultas frequentes

-- Pacientes: busca por clinica + psicologo + status (query mais comum)
CREATE INDEX IF NOT EXISTS idx_pacientes_clinica_psic_status 
ON pacientes(clinica_id, psicolog_id, status);

-- Pacientes: busca por CPF (validação de duplicatas)
CREATE INDEX IF NOT EXISTS idx_pacientes_cpf 
ON pacientes(cpf) WHERE cpf IS NOT NULL;

-- Sessões: busca por data (calendário/agenda)
CREATE INDEX IF NOT EXISTS idx_sessoes_data 
ON sessoes(data);

-- Sessões: busca por data + sala (disponibilidade)
CREATE INDEX IF NOT EXISTS idx_sessoes_data_sala 
ON sessoes(data, sala_id);

-- Sessões: busca por psicólogo + data (agenda do profissional)
CREATE INDEX IF NOT EXISTS idx_sessoes_psicologo_data 
ON sessoes(psicolog_id, data);

-- Sessões: busca por paciente (histórico do paciente)
CREATE INDEX IF NOT EXISTS idx_sessoes_paciente 
ON sessoes(paciente_id);

-- Pagamentos: busca por data (relatórios financeiros)
CREATE INDEX IF NOT EXISTS idx_pagamentos_data 
ON pagamentos(data);

-- Pagamentos: busca por data + tipo (análise por forma de pagamento)
CREATE INDEX IF NOT EXISTS idx_pagamentos_data_tipo 
ON pagamentos(data, tipo_pagamento_id);

-- Pagamentos: busca por paciente (histórico financeiro do paciente)
CREATE INDEX IF NOT EXISTS idx_pagamentos_paciente 
ON pagamentos(paciente_id);

-- Usuários: busca por username (login)
CREATE INDEX IF NOT EXISTS idx_usuarios_username 
ON usuarios(username);

-- Notificações: busca por usuário + lida (notificações não lidas)
CREATE INDEX IF NOT EXISTS idx_notificacoes_usuario_lida 
ON notificacoes(usuario_id, lida);

-- Logs: busca por data + ação (auditoria)
CREATE INDEX IF NOT EXISTS idx_logs_data_acao 
ON logs_auditoria(data_hora, acao);

-- Logs: busca por usuário (auditoria por usuário)
CREATE INDEX IF NOT EXISTS idx_logs_usuario 
ON logs_auditoria(usuario_id);

-- Mensagens: busca por tipo + ativa (mensagens do sistema)
CREATE INDEX IF NOT EXISTS idx_mensagens_tipo_ativa 
ON mensagens(tipo, ativa);

-- Download Requests: busca por status (processamento assíncrono)
CREATE INDEX IF NOT EXISTS idx_download_requests_status 
ON download_requests(status);

-- Download Requests: busca por usuário + data (histórico do usuário)
CREATE INDEX IF NOT EXISTS idx_download_requests_usuario_data 
ON download_requests(user_id, created_at DESC);

-- Comentários dos índices
-- idx_pacientes_clinica_psic_status: Otimiza listagem de pacientes por clínica/psicólogo
-- idx_sessoes_data: Otimiza busca de sessões por data (calendário)
-- idx_sessoes_data_sala: Otimiza verificação de disponibilidade de sala
-- idx_pagamentos_data: Otimiza relatórios financeiros por período
-- idx_notificacoes_usuario_lida: Otimiza listagem de notificações não lidas
-- idx_logs_data_acao: Otimiza consultas de auditoria

