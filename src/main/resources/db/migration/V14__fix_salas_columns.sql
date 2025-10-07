-- Corrigir colunas da tabela salas que podem estar faltando ou duplicadas

-- Adicionar colunas apenas se não existirem
DO $$
BEGIN
    -- ativa (não ativo)
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_name='salas' AND column_name='ativa') THEN
        ALTER TABLE salas ADD COLUMN ativa BOOLEAN DEFAULT TRUE NOT NULL;
    END IF;

    -- capacidade
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_name='salas' AND column_name='capacidade') THEN
        ALTER TABLE salas ADD COLUMN capacidade INTEGER;
    END IF;

    -- descricao
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_name='salas' AND column_name='descricao') THEN
        ALTER TABLE salas ADD COLUMN descricao VARCHAR(500);
    END IF;

    -- exclusiva (não exclusive)
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_name='salas' AND column_name='exclusiva') THEN
        ALTER TABLE salas ADD COLUMN exclusiva BOOLEAN DEFAULT FALSE;
    END IF;

    -- nome_responsavel
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_name='salas' AND column_name='nome_responsavel') THEN
        ALTER TABLE salas ADD COLUMN nome_responsavel VARCHAR(200);
    END IF;

    -- numero_responsavel
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_name='salas' AND column_name='numero_responsavel') THEN
        ALTER TABLE salas ADD COLUMN numero_responsavel VARCHAR(20);
    END IF;

    -- observacoes
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_name='salas' AND column_name='observacoes') THEN
        ALTER TABLE salas ADD COLUMN observacoes TEXT;
    END IF;

    -- permite_compartilhamento
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_name='salas' AND column_name='permite_compartilhamento') THEN
        ALTER TABLE salas ADD COLUMN permite_compartilhamento BOOLEAN DEFAULT TRUE;
    END IF;

    -- cor
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_name='salas' AND column_name='cor') THEN
        ALTER TABLE salas ADD COLUMN cor VARCHAR(7);
    END IF;

    -- psicologo_responsavel_id
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_name='salas' AND column_name='psicologo_responsavel_id') THEN
        ALTER TABLE salas ADD COLUMN psicologo_responsavel_id INTEGER REFERENCES psicologos(id);
    END IF;

    -- andar
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_name='salas' AND column_name='andar') THEN
        ALTER TABLE salas ADD COLUMN andar INTEGER;
    END IF;

    -- bloco
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_name='salas' AND column_name='bloco') THEN
        ALTER TABLE salas ADD COLUMN bloco VARCHAR(50);
    END IF;
END $$;

