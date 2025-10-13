-- Tabela de configurações do sistema
CREATE TABLE IF NOT EXISTS system_config (
    id BIGSERIAL PRIMARY KEY,
    config_key VARCHAR(255) UNIQUE NOT NULL,
    config_value TEXT,
    config_type VARCHAR(50),
    description VARCHAR(500),
    category VARCHAR(50),
    is_encrypted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255)
);

-- Índices para melhor performance
CREATE INDEX idx_system_config_key ON system_config(config_key);
CREATE INDEX idx_system_config_category ON system_config(category);

-- Comentários
COMMENT ON TABLE system_config IS 'Configurações do sistema';
COMMENT ON COLUMN system_config.config_key IS 'Chave única da configuração';
COMMENT ON COLUMN system_config.config_value IS 'Valor da configuração';
COMMENT ON COLUMN system_config.config_type IS 'Tipo da configuração (STRING, INTEGER, BOOLEAN, JSON)';
COMMENT ON COLUMN system_config.description IS 'Descrição da configuração';
COMMENT ON COLUMN system_config.category IS 'Categoria da configuração (SYSTEM, EMAIL, SECURITY, NOTIFICATIONS, BACKUP, LOGS)';
COMMENT ON COLUMN system_config.is_encrypted IS 'Indica se o valor está criptografado';


