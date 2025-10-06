-- =====================================================
-- Migration V10: Adicionar username único aos usuários
-- =====================================================

-- Adicionar coluna username (permitir NULL temporariamente)
ALTER TABLE users ADD COLUMN IF NOT EXISTS username VARCHAR(50);

-- Criar username baseado em clinica + psicologo para dados existentes
UPDATE users u
SET username = CONCAT('user_', u.clinica_id, '_', u.psicolog_id)
WHERE username IS NULL;

-- Tornar username obrigatório e único
ALTER TABLE users ALTER COLUMN username SET NOT NULL;
CREATE UNIQUE INDEX IF NOT EXISTS idx_users_username ON users(username);

-- Comentário
COMMENT ON COLUMN users.username IS 'Login único do usuário no sistema';

