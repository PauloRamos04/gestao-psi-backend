-- Corrigir colunas da tabela salas que podem estar faltando ou duplicadas

-- Adicionar colunas apenas se não existirem
DO $$
BEGIN
    -- ativo
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_name='salas' AND column_name='ativo') THEN
        ALTER TABLE salas ADD COLUMN ativo BOOLEAN DEFAULT TRUE;
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

    -- exclusive
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_name='salas' AND column_name='exclusive') THEN
        ALTER TABLE salas ADD COLUMN exclusive BOOLEAN DEFAULT FALSE;
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

    -- psicolog_responsavel_id
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_name='salas' AND column_name='psicolog_responsavel_id') THEN
        ALTER TABLE salas ADD COLUMN psicolog_responsavel_id INTEGER REFERENCES psicologos(id);
    END IF;
END $$;

