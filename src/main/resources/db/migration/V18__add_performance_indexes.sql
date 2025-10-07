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
ON sessoes(psicologo_id, data);

-- Sessões: busca por paciente (histórico do paciente)
CREATE INDEX IF NOT EXISTS idx_sessoes_paciente 
ON sessoes(paciente_id);

-- Pagamentos: busca por data (relatórios financeiros)
CREATE INDEX IF NOT EXISTS idx_pagamentos_data 
ON pagamentos(data_pagamento);

-- Pagamentos: busca por data + método (análise por forma de pagamento)
CREATE INDEX IF NOT EXISTS idx_pagamentos_data_metodo 
ON pagamentos(data_pagamento, metodo_pagamento);

-- Pagamentos: busca por sessão (verificar se sessão foi paga)
CREATE INDEX IF NOT EXISTS idx_pagamentos_sessao 
ON pagamentos(sessao_id);

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

COMMENT ON INDEX idx_pacientes_clinica_psic_status IS 'Otimiza listagem de pacientes por clínica/psicólogo';
COMMENT ON INDEX idx_sessoes_data IS 'Otimiza busca de sessões por data (calendário)';
COMMENT ON INDEX idx_sessoes_data_sala IS 'Otimiza verificação de disponibilidade de sala';
COMMENT ON INDEX idx_pagamentos_data IS 'Otimiza relatórios financeiros por período';
COMMENT ON INDEX idx_notificacoes_usuario_lida IS 'Otimiza listagem de notificações não lidas';
COMMENT ON INDEX idx_logs_data_acao IS 'Otimiza consultas de auditoria';

