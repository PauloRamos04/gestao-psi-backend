-- Adicionar campos de convênio à tabela de pagamentos

-- Adicionar campo para identificar se é pagamento via convênio
ALTER TABLE pagamentos ADD COLUMN IF NOT EXISTS eh_convenio BOOLEAN DEFAULT false;

-- Nome do convênio
ALTER TABLE pagamentos ADD COLUMN IF NOT EXISTS convenio VARCHAR(100);

-- Número da guia do convênio
ALTER TABLE pagamentos ADD COLUMN IF NOT EXISTS numero_guia VARCHAR(50);

-- Valor pago pelo convênio
ALTER TABLE pagamentos ADD COLUMN IF NOT EXISTS valor_convenio NUMERIC(10,2);

-- Valor de coparticipação (pago pelo paciente)
ALTER TABLE pagamentos ADD COLUMN IF NOT EXISTS valor_coparticipacao NUMERIC(10,2);

-- Criar índice para facilitar busca por convênio
CREATE INDEX IF NOT EXISTS idx_pagamentos_convenio ON pagamentos(eh_convenio);
CREATE INDEX IF NOT EXISTS idx_pagamentos_convenio_nome ON pagamentos(convenio);

-- Comentários nas colunas
COMMENT ON COLUMN pagamentos.eh_convenio IS 'Indica se o pagamento foi via convênio';
COMMENT ON COLUMN pagamentos.convenio IS 'Nome do convênio de saúde';
COMMENT ON COLUMN pagamentos.numero_guia IS 'Número da guia/autorização do convênio';
COMMENT ON COLUMN pagamentos.valor_convenio IS 'Valor que será pago pelo convênio';
COMMENT ON COLUMN pagamentos.valor_coparticipacao IS 'Valor de coparticipação pago pelo paciente';




