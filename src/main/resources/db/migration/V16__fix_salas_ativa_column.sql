-- Corrigir problema de coluna 'ativa' vs 'ativo' na tabela salas

DO $$
BEGIN
    -- Se existe coluna 'ativo', renomear para 'ativa'
    IF EXISTS (SELECT 1 FROM information_schema.columns 
               WHERE table_name='salas' AND column_name='ativo') THEN
        ALTER TABLE salas RENAME COLUMN ativo TO ativa;
    END IF;

    -- Se não existe nenhuma das duas, criar 'ativa'
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_name='salas' AND column_name='ativa') 
       AND NOT EXISTS (SELECT 1 FROM information_schema.columns 
                       WHERE table_name='salas' AND column_name='ativo') THEN
        ALTER TABLE salas ADD COLUMN ativa BOOLEAN DEFAULT TRUE NOT NULL;
    END IF;

    -- Garantir que ativa não seja null
    IF EXISTS (SELECT 1 FROM information_schema.columns 
               WHERE table_name='salas' AND column_name='ativa') THEN
        ALTER TABLE salas ALTER COLUMN ativa SET DEFAULT TRUE;
        ALTER TABLE salas ALTER COLUMN ativa SET NOT NULL;
        UPDATE salas SET ativa = TRUE WHERE ativa IS NULL;
    END IF;
END $$;

-- Criar índice se não existir
CREATE INDEX IF NOT EXISTS idx_salas_status ON salas (ativa);

