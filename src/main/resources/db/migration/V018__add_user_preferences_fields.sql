-- Adicionar campos de preferências do usuário
ALTER TABLE users ADD COLUMN IF NOT EXISTS lembretes_sessao BOOLEAN DEFAULT true;
ALTER TABLE users ADD COLUMN IF NOT EXISTS notificacoes_pagamento BOOLEAN DEFAULT true;

-- Comentários para documentação
COMMENT ON COLUMN users.lembretes_sessao IS 'Indica se o usuário deseja receber lembretes de sessões agendadas';
COMMENT ON COLUMN users.notificacoes_pagamento IS 'Indica se o usuário deseja receber notificações sobre pagamentos pendentes';

